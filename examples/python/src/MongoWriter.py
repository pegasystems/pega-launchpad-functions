import csv
import ssl
import certifi
import requests
from datetime import datetime
from concurrent.futures import ThreadPoolExecutor, as_completed
from pymongo import MongoClient
from pymongo.write_concern import WriteConcern
from pymongo.errors import ConnectionFailure, OperationFailure, ConfigurationError
from urllib.parse import urlparse, parse_qs, urlencode, urlunparse


def convert_timestamps_in_document(document):
    """
    Convert ISO 8601 timestamp strings to Python datetime objects for BSON compatibility.

    Args:
        document: Dictionary that may contain timestamp fields

    Returns:
        Dictionary with timestamp fields converted to datetime objects
    """
    if not isinstance(document, dict):
        return document

    converted_doc = {}
    for key, value in document.items():
        # Check if this looks like a timestamp column
        if isinstance(value, str) and key.lower() in ['timestamp', 'time', 'datetime', 'date', 'created_at', 'updated_at']:
            try:
                # Try parsing ISO 8601 format (e.g., 2026-02-27T19:45:00Z)
                if value.endswith('Z'):
                    dt = datetime.strptime(value, '%Y-%m-%dT%H:%M:%SZ')
                else:
                    # Try without Z suffix
                    dt = datetime.fromisoformat(value.replace('Z', '+00:00'))
                converted_doc[key] = dt
            except (ValueError, AttributeError):
                # If parsing fails, keep as string
                converted_doc[key] = value
        else:
            converted_doc[key] = value

    return converted_doc


def handler(event, context):
    """
    MongoDB operations handler for Pega Launchpad custom functions.

    Performs Insert Document and Bulk Write operations using pymongo driver.
    Creates a new stateless connection for each invocation.

    Args:
        event: Dictionary containing:
            - connection_string (Text): MongoDB connection string with credentials
            - operation (Text): Operation type - 'insert_one' or 'bulk_write'
            - namespace (Text): Database and collection in format 'database.collection'
            - document (Object): Single document for insert_one operation
            - csv_url (Text): Signed URL to CSV file for bulk_write operation
            - batch_size (Integer): Number of documents per insert_many batch (default: 10000)
        context: Lambda context object (unused)

    Returns:
        Dictionary with statusCode and operation results or error message
    """

    # Capture the timestamp when this function was called
    function_call_timestamp = datetime.utcnow()

    # Extract and validate required parameters
    connection_string = event.get('connection_string')
    operation = event.get('operation')
    namespace = event.get('namespace')

    if not connection_string:
        return {
            'statusCode': 400,
            'error': 'connection_string is required'
        }

    if not operation:
        return {
            'statusCode': 400,
            'error': 'operation is required'
        }

    if not namespace:
        return {
            'statusCode': 400,
            'error': 'namespace is required'
        }

    # Parse namespace into database and collection
    namespace_parts = namespace.split('.', 1)
    if len(namespace_parts) != 2:
        return {
            'statusCode': 400,
            'error': 'namespace must be in format "database.collection"'
        }

    database_name = namespace_parts[0]
    collection_name = namespace_parts[1]

    # Validate operation type
    if operation not in ['insert_one', 'bulk_write']:
        return {
            'statusCode': 400,
            'error': f'Invalid operation: {operation}. Supported operations: insert_one, bulk_write'
        }

    client = None
    try:
        # Create new MongoDB client connection (stateless)
        # Configure write concern with w=0 (unacknowledged writes)
        write_concern = WriteConcern(w=0)
        client = MongoClient(
            connection_string,
            serverSelectionTimeoutMS=10000,
            connectTimeoutMS=10000,

        )

        # Test connection
        client.admin.command('ping', maxTimeMS=5000)

        # Get database and collection
        db = client[database_name].with_options(write_concern=WriteConcern(w=0))
        collection = db[collection_name]

        # Execute operation
        if operation == 'insert_one':
            document = event.get('document')
            if not document:
                return {
                    'statusCode': 400,
                    'error': 'document is required for insert_one operation'
                }

            # Convert timestamp strings to BSON datetime objects
            document = convert_timestamps_in_document(document)

            result = collection.insert_one(document)
            return {
                'statusCode': 200,
                'operation': 'insert_one',
                'inserted_id': str(result.inserted_id),
                'acknowledged': result.acknowledged
            }

        elif operation == 'bulk_write':
            csv_url = event.get('csv_url')
            if not csv_url:
                return {
                    'statusCode': 400,
                    'error': 'csv_url is required for bulk_write operation'
                }

            # Fetch and parse CSV file from URL

            try:
                documents = []
                with requests.get(csv_url, stream=True) as r:
                    r.raise_for_status()
                    lines = (line.decode('utf-8') for line in r.iter_lines())
                    reader = csv.DictReader(lines)

                    for row in reader:
                        # Convert each CSV row into a document
                        # Remove empty string values and convert to appropriate types
                        document = {}
                        for key, value in row.items():
                            if value:  # Skip empty values
                                # Check if this looks like a timestamp column and try to parse it
                                if key.lower() in ['timestamp', 'time', 'datetime', 'date', 'created_at', 'updated_at']:
                                    try:
                                        # Try parsing ISO 8601 format (e.g., 2026-02-27T19:45:00Z)
                                        if value.endswith('Z'):
                                            dt = datetime.strptime(value, '%Y-%m-%dT%H:%M:%SZ')
                                        else:
                                            # Try without Z suffix
                                            dt = datetime.fromisoformat(value.replace('Z', '+00:00'))
                                        document[key] = dt
                                    except (ValueError, AttributeError):
                                        # If parsing fails, keep as string
                                        document[key] = value
                                else:
                                    # Try to convert to number if possible
                                    try:
                                        # Try integer first
                                        if '.' not in value:
                                            document[key] = int(value)
                                        else:
                                            document[key] = float(value)
                                    except (ValueError, AttributeError):
                                        # Keep as string if not a number
                                        document[key] = value

                        if document:  # Only add non-empty documents
                            documents.append(document)

                if len(documents) == 0:
                    return {
                        'statusCode': 400,
                        'error': 'CSV file contains no valid data rows'
                    }

                # Add bulkwritetimestamp to each document
                for doc in documents:
                    doc['bulkwritetimestamp'] = function_call_timestamp

                # Insert all documents from CSV in batches concurrently
                batch_size = int(event.get('batch_size', 10000) or 10000)
                max_workers = min(10, (len(documents) // batch_size) + 1)
                batches = [documents[i:i + batch_size] for i in range(0, len(documents), batch_size)]

                def insert_batch(batch):
                    return collection.insert_many(batch, ordered=False)

                total_inserted = 0
                errors = []

                with ThreadPoolExecutor(max_workers=max_workers) as executor:
                    futures = {executor.submit(insert_batch, batch): idx for idx, batch in enumerate(batches)}

                    for future in as_completed(futures):
                        try:
                            result = future.result()
                            total_inserted += len(result.inserted_ids)
                        except Exception as e:
                            errors.append(f'Batch {futures[future]}: {str(e)}')

                response = {
                    'statusCode': 200,
                    'operation': 'bulk_write',
                    'csv_count': len(documents),
                    'inserted_count': total_inserted,
                    'batch_count': len(batches),
                    'acknowledged': True
                }

                if errors:
                    response['errors'] = errors

                return response

            except requests.exceptions.RequestException as e:
                return {
                    'statusCode': 400,
                    'error': f'Failed to fetch CSV file: {str(e)}'
                }
            except csv.Error as e:
                return {
                    'statusCode': 400,
                    'error': f'Failed to parse CSV file: {str(e)}'
                }

    except ConnectionFailure as e:
        return {
            'statusCode': 500,
            'error': f'MongoDB connection failed: {str(e)}'
        }

    except ConfigurationError as e:
        return {
            'statusCode': 400,
            'error': f'MongoDB configuration error: {str(e)}'
        }

    except OperationFailure as e:
        return {
            'statusCode': 400,
            'error': f'MongoDB operation failed: {str(e)}'
        }

    except Exception as e:
        return {
            'statusCode': 500,
            'error': f'Unexpected error: {str(e)}'
        }

    finally:
        # Close connection (stateless function)
        if client:
            client.close()


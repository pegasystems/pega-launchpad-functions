from bson import json_util
import json
from pymongo import MongoClient
from pymongo.errors import ConnectionFailure, OperationFailure, ConfigurationError


def handler(event, context):
    """
    Reads all documents from a MongoDB collection and returns them as a JSON array.

    Args:
        event: Dictionary containing:
            - connection_string (Text): MongoDB connection string with credentials
            - namespace (Text): Database and collection in format 'database.collection'
        context: Lambda context object (unused)

    Returns:
        Dictionary with statusCode and documents array or error message.
    """

    # Extract and validate required parameters
    connection_string = event.get('connection_string')
    namespace = event.get('namespace')
    clear_after_read = event.get('clear_after_read', False)

    if not connection_string:
        return {
            'statusCode': 400,
            'error': 'connection_string is required'
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

    client = None
    try:
        client = MongoClient(
            connection_string,
            serverSelectionTimeoutMS=10000,
            connectTimeoutMS=10000,
        )

        # Test connection
        client.admin.command('ping', maxTimeMS=5000)

        # Get database and collection
        db = client[database_name]
        collection = db[collection_name]

        # Find all documents (no filter)
        cursor = collection.find()

        # Convert documents to JSON-serializable format using bson json_util
        # This handles ObjectId, datetime, and other BSON types
        documents = json.loads(json_util.dumps(list(cursor)))

        # Delete all documents from the collection if requested
        deleted_count = 0
        if clear_after_read:
            delete_result = collection.delete_many({})
            deleted_count = delete_result.deleted_count

        return {
            'statusCode': 200,
            'operation': 'find',
            'namespace': namespace,
            'count': len(documents),
            'documents': documents,
            'cleared': clear_after_read,
            'deleted_count': deleted_count
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
        if client:
            client.close()


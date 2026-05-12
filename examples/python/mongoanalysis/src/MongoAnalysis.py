from datetime import datetime
from pymongo import MongoClient
from pymongo.errors import ConnectionFailure, OperationFailure, ConfigurationError


def handler(event, context):
    """
    MongoDB analysis handler for Pega Launchpad custom functions.

    Queries a time series collection and returns the top 5 sensors with the
    widest temperature variation in a single day, along with the date, min
    temperature, and max temperature observed on those dates.

    Args:
        event: Dictionary containing:
            - connection_string (Text): MongoDB connection string with credentials
            - namespace (Text): Database and collection in format 'database.collection'
        context: Lambda context object (unused)

    Returns:
        Dictionary with statusCode and analysis results or error message.
        Results include a 'top_sensors' list with sensor_id, date,
        min_temp, max_temp, and temp_range for the top 5 widest daily swings.
    """

    # Extract and validate required parameters
    connection_string = event.get('connection_string')
    namespace = event.get('namespace')

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
        # Create new MongoDB client connection (stateless)
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

        # Aggregation pipeline:
        # 1. Group by sensor_id + date (truncated to day) to get min/max temp per sensor per day
        # 2. Compute the temperature range (max - min)
        # 3. Sort descending by range
        # 4. Limit to top 5
        pipeline = [
            {
                '$group': {
                    '_id': {
                        'sensor_id': '$sensor_id',
                        'date': {
                            '$dateTrunc': {
                                'date': '$timestamp',
                                'unit': 'day'
                            }
                        }
                    },
                    'min_temp': {'$min': '$temperature'},
                    'max_temp': {'$max': '$temperature'},
                    'reading_count': {'$sum': 1}
                }
            },
            {
                '$addFields': {
                    'temp_range': {'$subtract': ['$max_temp', '$min_temp']}
                }
            },
            {
                '$sort': {'temp_range': -1}
            },
            {
                '$limit': 5
            },
            {
                '$project': {
                    '_id': 0,
                    'sensor_id': '$_id.sensor_id',
                    'date': {
                        '$dateToString': {
                            'format': '%Y-%m-%d',
                            'date': '$_id.date'
                        }
                    },
                    'min_temp': {'$round': ['$min_temp', 2]},
                    'max_temp': {'$round': ['$max_temp', 2]},
                    'temp_range': {'$round': ['$temp_range', 2]},
                    'reading_count': 1
                }
            }
        ]

        results = list(collection.aggregate(pipeline))

        return {
            'statusCode': 200,
            'operation': 'analyze_temperature_variation',
            'namespace': namespace,
            'result_count': len(results),
            'top_sensors': results
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


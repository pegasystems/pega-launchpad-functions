import json

def handler(event, context):
    num1 = float(event.get('num1', 0))
    num2 = float(event.get('num2', 1))

    addition = num1 + num2
    subtraction = num1 - num2
    multiplication = num1 * num2
    division = num1 / num2 if num2 != 0 else 'undefined'

    # Return the results
    return {
        'statusCode': 200,
        'body': json.dumps({
            'addition': addition,
            'subtraction': subtraction,
            'multiplication': multiplication,
            'division': division
        })
    }
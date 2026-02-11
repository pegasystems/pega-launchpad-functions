import requests
import csv
import json

def handler(event, context):
    """
    Analyzes voltage data from a CSV file accessible via a signed URL.

    Fetches the CSV file from the provided signed URL, reads the voltage data,
    and identifies the rows with the maximum and minimum voltage values.
    Returns both rows along with their voltage data as a JSON string.

    Args:
        event: Dictionary containing 'SignedURL' - the URL to fetch the CSV file from
        context: AWS Lambda context object (unused)

    Returns:
        JSON string containing 'max_voltage_row' and 'min_voltage_row' with their data,
        or an error response if SignedURL is missing
    """
    url = event.get('SignedURL')
    if not url:
        return {'statusCode': 400, 'body': json.dumps({'error': 'SignedURL missing'})}

    max_row = None
    min_row = None
    max_voltage = None
    min_voltage = None

    with requests.get(url, stream=True) as r:
        r.raise_for_status()
        lines = (line.decode('utf-8') for line in r.iter_lines())
        reader = csv.DictReader(lines)
        for row in reader:
            try:
                voltage = float(row['voltage'])
            except (KeyError, ValueError):
                continue
            if max_voltage is None or voltage > max_voltage:
                max_voltage = voltage
                max_row = row.copy()
            if min_voltage is None or voltage < min_voltage:
                min_voltage = voltage
                min_row = row.copy()

    # Convert voltage to decimal in the response
    if max_row:
        max_row['voltage'] = float(max_row['voltage'])
    if min_row:
        min_row['voltage'] = float(min_row['voltage'])

    result = {
        'max_voltage_row': max_row,
        'min_voltage_row': min_row
    }
    return json.dumps(result)



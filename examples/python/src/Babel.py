from babel.numbers import format_currency as babel_format_currency


def format_currency(event, context):
    """
    Formats a currency amount using the Babel library with locale support.

    Takes a numeric amount, currency code, and optional locale, and returns a formatted currency string
    using locale-aware formatting from the Babel library.

    Args:
        event: Dictionary containing:
            - 'amount': The numeric value (double) to format
            - 'currency': The ISO 4217 currency code (e.g., 'USD', 'EUR', 'GBP')
            - 'locale': Optional locale string (e.g., 'en_US', 'fr_FR', 'de_DE'). Defaults to 'en_US'
        context: AWS Lambda context object (unused)

    Returns:
        Formatted currency string from Babel (e.g., '$1,234.56'), or error dictionary if conversion fails
    """
    try:
        amount = float(event.get('amount'))
        currency = event.get('currency', 'USD')
        locale = event.get('locale') or 'en_US'

        # Format the currency using Babel
        return babel_format_currency(number=amount, currency=currency, locale=locale)

    except (ValueError, TypeError) as e:
        return {
            'statusCode': 400,
            'error': f'Invalid amount: {str(e)}'
        }
    except Exception as e:
        return {
            'statusCode': 500,
            'error': f'Error formatting currency: {str(e)}'
        }

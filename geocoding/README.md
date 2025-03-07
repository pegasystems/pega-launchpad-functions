# Geocoding examples

<!-- TOC -->
* [Geocoding examples](#Geocoding-examples)
  * [Get Address Components](#Get-Address-Components)
<!-- TOC -->

## Get Address Components

For a given address value, use the [Google Maps Geocoding API](https://developers.google.com/maps/documentation/geocoding/requests-geocoding) to attempt to break it down into address components.

### Java code info
- **Class**: [com.pega.launchpad.geocoding.Geocoding](src/main/java/com/pega/launchpad/geocoding/Geocoding.java)
- **Method**: getAddressComponents

### Function rule configuration

- Function handler: com.pega.launchpad.geocoding.Geocoding::getAddressComponents
- Input parameters:
  - **key (Text)** - Your google maps API key (required)
  - **address (Text)** - The address to geocode (required)
- Output parameters:
  - **Type**: *[choose one of your application's case types where you want to store the results]*
  - **Cardinality**: Single object
  - Note: JSON Transform rule will be required
    
### JSON Transform rule configuration

1. Create JSON Transform rule with:
- **Name**: the same name as your function (not required, just easier for author)
- **Purpose**: Deserialize (JSON to Pega Object)
- **Library**: Same as the case type chosen for the Function rule's output parameter **Type**
- **Top level structure**: Single object
2. Add example JSON response:
- **System name**: any identifier you want
- **JSON sample**: See below example
- For example, you need to use a JSON sample like this to properly define the JSON transform:
```
{
  "FormattedAddress": "64, Hitech City Rd, Sri Rama Colony, Madhapur, Hyderabad, Telangana 500033, India",
  "PlaceID": "ElE2NCwgSGl0ZWNoIENpdHkgUmQsIFNyaSBSYW1hIENvbG9ueSwgTWFkaGFwdXIsIEh5ZGVyYWJhZCwgVGVsYW5nYW5hIDUwMDAzMywgSW5kaWEiMBIuChQKEgmNNc1UW5HLOxEhMClOdic3ERBAKhQKEgktPoLkXZHLOxHw9wIrrYyAmg",
  "Country": "India",
  "CountryShort": "IN",
  "PostalCode": "500033",
  "PostalCodeSuffix": "null",
  "StreetNumber": "64",
  "Route": "Hitech City Road",
  "Locality": "Hyderabad",
  "Sublocality": "null",
  "AdminAreaLevel1": "Telangana",
  "AdminAreaLevel2": "null",
  "AdminAreaLevel3": "Rangareddy",
  "AdminAreaLevel4": "null",
  "AdminAreaLevel5": "null",
  "Premise": "null",
  "Latitude": "17.4399979",
  "Longitude": "78.3955603"
}
```
3. Map your data:
- Map values from the json to the appropriate fields in your case
  

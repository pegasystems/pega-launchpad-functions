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

The method returns a flat set of text values:

* FormattedAddress: is a string containing the human-readable address of this location.
* PlaceID: Google place ID
* Country: indicates the national political entity, and is typically the highest order type returned by the Geocoder.
* CountryShort: abbreviation of Country
* PostalCode: indicates a postal code as used to address postal mail within the country.
* PostalCodeSuffix: optional postal code suffix
* StreetNumber: The house/building number
* Route: indicates a named route (such as "US 101").
* Locality: indicates an incorporated city or town political entity.
* Sublocality: indicates a first-order civil entity below a locality.
* AdminAreaLevel1: indicates a first-order civil entity below the country level. Within the United States, these administrative levels are states. Not all nations exhibit these administrative levels.
* AdminAreaLevel2: indicates a second-order civil entity below the country level. Within the United States, these administrative levels are counties.
* AdminAreaLevel3: indicates a third-order civil entity below the country level. This type indicates a minor civil division. Not all nations exhibit these administrative levels.
* AdminAreaLevel4: indicates a fourth-order civil entity below the country level. This type indicates a minor civil division. Not all nations exhibit these administrative levels.
* AdminAreaLevel5: indicates a fifth-order civil entity below the country level. This type indicates a minor civil division. Not all nations exhibit these administrative levels.
* Premise: indicates a named location, usually a building or collection of buildings with a common name.
* Latitude: Latitude
* Longitude: Longitude

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
  

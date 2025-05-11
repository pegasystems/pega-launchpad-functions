package com.pega.launchpad.geocoding;

/**
 * POJO for the address components returned from a geocoding API request
 */
public class AddressComponents {
    public String FormattedAddress; // is a string containing the human-readable address of this location.
    public String PlaceID;
    public String Country; // indicates the national political entity, and is typically the highest order type returned by the Geocoder.
    public String CountryShort;
    public String PostalCode; // indicates a postal code as used to address postal mail within the country.
    public String PostalCodeSuffix;
    public String StreetNumber;
    public String Route; // indicates a named route (such as "US 101").
    public String Locality; // indicates an incorporated city or town political entity.
    public String Sublocality; // indicates a first-order civil entity below a locality.
    public String AdminAreaLevel1; // indicates a first-order civil entity below the country level. Within the United States, these administrative levels are states. Not all nations exhibit these administrative levels.
    public String AdminAreaLevel2; // indicates a second-order civil entity below the country level. Within the United States, these administrative levels are counties.
    public String AdminAreaLevel3; // indicates a third-order civil entity below the country level. This type indicates a minor civil division. Not all nations exhibit these administrative levels.
    public String AdminAreaLevel4; // indicates a fourth-order civil entity below the country level. This type indicates a minor civil division. Not all nations exhibit these administrative levels.
    public String AdminAreaLevel5; // indicates a fifth-order civil entity below the country level. This type indicates a minor civil division. Not all nations exhibit these administrative levels.
    public String Premise; // indicates a named location, usually a building or collection of buildings with a common name.
    public double Latitude;
    public double Longitude;

    public String toString() {
        String s = "{";
        s += "\"FormattedAddress\":\"" + FormattedAddress + "\",";
        s += "\"PlaceID\":\"" + PlaceID + "\",";
        s += "\"Country\":\"" + Country + "\",";
        s += "\"CountryShort\":\"" + CountryShort + "\",";
        s += "\"PostalCode\":\"" + PostalCode + "\",";
        s += "\"PostalCodeSuffix\":\"" + PostalCodeSuffix + "\",";
        s += "\"StreetNumber\":\"" + StreetNumber + "\",";
        s += "\"Route\":\"" + Route + "\",";
        s += "\"Locality\":\"" + Locality + "\",";
        s += "\"Sublocality\":\"" + Sublocality + "\",";
        s += "\"AdminAreaLevel1\":\"" + AdminAreaLevel1 + "\",";
        s += "\"AdminAreaLevel2\":\"" + AdminAreaLevel2 + "\",";
        s += "\"AdminAreaLevel3\":\"" + AdminAreaLevel3 + "\",";
        s += "\"AdminAreaLevel4\":\"" + AdminAreaLevel4 + "\",";
        s += "\"AdminAreaLevel5\":\"" + AdminAreaLevel5 + "\",";
        s += "\"Premise\":\"" + Premise + "\",";
        s += "\"Latitude\":\"" + Latitude + "\",";
        s += "\"Longitude\":\"" + Longitude + "\"";
        s += "}";
        return s;
    }
}

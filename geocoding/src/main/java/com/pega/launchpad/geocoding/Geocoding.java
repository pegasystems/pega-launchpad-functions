package com.pega.launchpad.geocoding;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.model.AddressComponent;
import com.google.maps.model.GeocodingResult;

import java.util.Map;

/**
 * Class that uses the Google Geocoding API to provide a set of address components for a specified full address.
 */
public class Geocoding {

    /**
     * Given a full address, and a Google Maps API key, attempt to return the various parts of the address components.
     * @param inputMap requires 'Key' (google maps api key) and 'Address' (full address)
     * @return AddressComponents The flattened components of the specified address
     */
    public static AddressComponents getAddressComponents(Map<String, String> inputMap) {
        AddressComponents ac = new AddressComponents();
        try {
            String key = inputMap.get("Key");
            if (key == null || key.isEmpty()) throw new IllegalArgumentException("'Key' must be provided in inputMap for google maps API key");
            String address = inputMap.get("Address");
            if (address == null || address.isEmpty()) throw new IllegalArgumentException("'Address' must be provided in inputMap for address to try and geocode");

            GeoApiContext context = new GeoApiContext.Builder()
                    .apiKey(key)
                    .build();
            GeocodingApiRequest request = GeocodingApi.geocode(context, address);
            GeocodingResult[] results = request.await();
            if (results.length == 0) return ac;

            GeocodingResult result = results[0];
            ac.Latitude = result.geometry.location.lat;
            ac.Longitude = result.geometry.location.lng;
            ac.PlaceID = result.placeId;
            ac.FormattedAddress = result.formattedAddress;

            try (context) {
                for (AddressComponent comp : result.addressComponents) {

                    switch (comp.types[0]) {
                        case LOCALITY:
                            ac.Locality = comp.longName;
                            break;
                        case SUBLOCALITY:
                            ac.Sublocality = comp.longName;
                            break;
                        case COUNTRY:
                            ac.Country = comp.longName;
                            ac.CountryShort = comp.shortName;
                            break;
                        case ADMINISTRATIVE_AREA_LEVEL_1:
                            ac.AdminAreaLevel1 = comp.longName;
                            break;
                        case ADMINISTRATIVE_AREA_LEVEL_2:
                            ac.AdminAreaLevel2 = comp.longName;
                            break;
                        case ADMINISTRATIVE_AREA_LEVEL_3:
                            ac.AdminAreaLevel3 = comp.longName;
                            break;
                        case ADMINISTRATIVE_AREA_LEVEL_4:
                            ac.AdminAreaLevel4 = comp.longName;
                            break;
                        case ADMINISTRATIVE_AREA_LEVEL_5:
                            ac.AdminAreaLevel5 = comp.longName;
                            break;
                        case STREET_NUMBER:
                            ac.StreetNumber = comp.longName;
                            break;
                        case ROUTE:
                            ac.Route = comp.longName;
                            break;
                        case POSTAL_CODE:
                            ac.PostalCode = comp.longName;
                            break;
                        case POSTAL_CODE_SUFFIX:
                            ac.PostalCodeSuffix = comp.longName;
                            break;
                        case PREMISE:
                            ac.Premise = comp.longName;
                            break;
                    }
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        return ac;
    }

}

package com.ada.genealogyapp.gedcom.mappers;

import com.ada.genealogyapp.location.type.LocationType;

public class LocationMapper {

    public static LocationType determineType(String partName, String country, String state, String city) {
        if (partName.equalsIgnoreCase(country)) {
            return LocationType.COUNTRY;
        }
        if (partName.equalsIgnoreCase(state)) {
            return LocationType.STATE;
        }
        if (partName.equalsIgnoreCase(city)) {
            return LocationType.CITY;
        }
        return LocationType.UNKNOWN;
    }
}

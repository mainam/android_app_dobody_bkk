package com.dobody.bkk.dataaccess;

import java.util.HashMap;

/**
 * Created by MaiNam on 12/23/2016.
 */

public class CountryInfo {
    static HashMap<String, String> country;

    public static HashMap<String, String> getCountries() {
        if (country == null) {
            country = new HashMap<>();
            country.put("SG", "SINGAPORE");
            country.put("TW", "TAIWAN");
            country.put("BN", "BRUNEI");
        }
        return country;
    }

    public static String getCountryName(String countryCode) {
        if(countryCode==null)
            return "";
        countryCode=countryCode.toUpperCase();
        if (getCountries().containsKey(countryCode))
            return getCountries().get(countryCode);
        return "";
    }
}

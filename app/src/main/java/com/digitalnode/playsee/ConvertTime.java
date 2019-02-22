package com.digitalnode.playsee;

import android.util.Log;

import java.util.HashMap;
import java.util.regex.Pattern;

public class ConvertTime {
    private static HashMap<String, String> regexMap = new HashMap<>();
    private static String regex2two = "(?<=[^\\d])(\\d)(?=[^\\d])";
    private static String two = "0$1";

    private String fmtTime;

    public ConvertTime(String date) {

        regexMap.put("PT(\\d\\d)S", "00:$1");
        regexMap.put("PT(\\d\\d)M", "$1:00");
        regexMap.put("PT(\\d\\d)H", "$1:00:00");
        regexMap.put("PT(\\d\\d)M(\\d\\d)S", "$1:$2");
        regexMap.put("PT(\\d\\d)H(\\d\\d)S", "$1:00:$2");
        regexMap.put("PT(\\d\\d)H(\\d\\d)M", "$1:$2:00");
        regexMap.put("PT(\\d\\d)H(\\d\\d)M(\\d\\d)S", "$1:$2:$3");

        String d = date.replaceAll(regex2two, two);
        String regex = getRegex(d);
        if (regex == null) {
            Log.d("error-parsing-time", d + ": invalid");
        }
        String newDate = d.replaceAll(regex, regexMap.get(regex));
        fmtTime = newDate;
    }

    private static String getRegex(String date) {
        for (String r : regexMap.keySet())
            if (Pattern.matches(r, date))
                return r;
        return null;
    }

    public String getFmtTime()
    {
        return fmtTime;
    }
}

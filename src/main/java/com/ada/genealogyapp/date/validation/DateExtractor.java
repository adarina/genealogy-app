package com.ada.genealogyapp.date.validation;


import com.ada.genealogyapp.date.model.Date;
import com.ada.genealogyapp.date.model.PartialDate;
import com.ada.genealogyapp.date.type.QualityType;
import com.ada.genealogyapp.date.type.TypeType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateExtractor {

    public static Date parseDate(String input) {
        Date date = new Date();

        Pattern fullDatePattern = Pattern.compile("^(EXACT|ESTIMATED|CALCULATED)?\\s*(BEFORE|AFTER|ABOUT|EXACT|RANGE)?\\s*(\\d{2})\\.(\\d{2})\\.(\\d{4})$");
        Pattern yearOnlyPattern = Pattern.compile("^(EXACT|ESTIMATED|CALCULATED)?\\s*(BEFORE|AFTER|ABOUT|EXACT|RANGE)?\\s*(\\d{4})$");
        Pattern rangePattern = Pattern.compile("^(EXACT|ESTIMATED|CALCULATED)?\\s*RANGE\\s*(\\d{2})\\.(\\d{2})\\.(\\d{4})\\s*and\\s*(\\d{2})\\.(\\d{2})\\.(\\d{4})$");

        Matcher matcher;

        matcher = fullDatePattern.matcher(input);
        if (matcher.matches()) {
            setQualityAndType(date, matcher.group(1), matcher.group(2));
            date.setFirstDate(createPartialDate(
                    Integer.parseInt(matcher.group(5)),
                    Integer.parseInt(matcher.group(4)),
                    Integer.parseInt(matcher.group(3))
            ));
            return date;
        }

        matcher = yearOnlyPattern.matcher(input);
        if (matcher.matches()) {
            setQualityAndType(date, matcher.group(1), matcher.group(2));
            date.setFirstDate(createPartialDate(
                    Integer.parseInt(matcher.group(3)),
                    null,
                    null
            ));
            return date;
        }

        matcher = rangePattern.matcher(input);
        if (matcher.matches()) {
            setQualityAndType(date, matcher.group(1), "RANGE");
            date.setFirstDate(createPartialDate(
                    Integer.parseInt(matcher.group(4)),
                    Integer.parseInt(matcher.group(3)),
                    Integer.parseInt(matcher.group(2))
            ));
            date.setSecondDate(createPartialDate(
                    Integer.parseInt(matcher.group(7)),
                    Integer.parseInt(matcher.group(6)),
                    Integer.parseInt(matcher.group(5))
            ));
            return date;
        }

        throw new IllegalArgumentException("Nieprawidłowy format daty: " + input);
    }

    private static void setQualityAndType(Date date, String quality, String type) {
        if (quality != null) {
            switch (quality.toUpperCase()) {
                case "EXACT":
                    date.setQualityType(QualityType.EXACT);
                    break;
                case "ESTIMATED":
                    date.setQualityType(QualityType.ESTIMATED);
                    break;
                case "CALCULATED":
                    date.setQualityType(QualityType.CALCULATED);
                    break;
                default:
                    date.setQualityType(QualityType.EXACT);
            }
        } else {
            date.setQualityType(QualityType.EXACT);
        }

        if (type != null) {
            switch (type.toUpperCase()) {
                case "BEFORE":
                    date.setTypeType(TypeType.BEFORE);
                    break;
                case "AFTER":
                    date.setTypeType(TypeType.AFTER);
                    break;
                case "ABOUT":
                    date.setTypeType(TypeType.ABOUT);
                    break;
                case "EXACT":
                    date.setTypeType(TypeType.EXACT);
                    break;
                case "RANGE":
                    date.setTypeType(TypeType.RANGE);
                    break;
                default:
                    date.setTypeType(TypeType.EXACT); // Domyślnie EXACT
            }
        } else {
            date.setTypeType(TypeType.EXACT); // Domyślnie EXACT
        }
    }

    private static PartialDate createPartialDate(Integer year, Integer month, Integer day) {
        PartialDate partialDate = new PartialDate();
        partialDate.setYear(year);
        partialDate.setMonth(month);
        partialDate.setDay(day);
        return partialDate;
    }

//    public static void main(String[] args) {
//        // Przykładowe daty wejściowe
//        String[] testDates = {
//                "12.12.1990",
//                "1800",
//                "EXACT 12.12.1990",
//                "ESTIMATED 1800",
//                "BEFORE 1700",
//                "ABOUT 13.04.1234",
//                "EXACT BEFORE 1700",
//                "RANGE 12.12.1800 and 13.01.1810"
//        };
//
//        for (String testDate : testDates) {
//            try {
//                Date parsedDate = parseDate(testDate);
//                System.out.println("Input: " + testDate);
//                System.out.println("Parsed: " + parsedDate);
//            } catch (Exception e) {
//                System.out.println("Error parsing date: " + testDate + " (" + e.getMessage() + ")");
//            }
//        }
//    }
}

package com.ada.genealogyapp.date.service;


import com.ada.genealogyapp.date.model.Date;
import com.ada.genealogyapp.date.model.PartialDate;
import com.ada.genealogyapp.date.type.MonthType;
import com.ada.genealogyapp.date.type.QualityType;
import com.ada.genealogyapp.date.type.KindType;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.isNull;

@Slf4j
public class DateExtractor {

    private static final String MONTH_ABBR = "(?:JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC)";
    private static final String PREFIX = "^\\s*(EXACT|EST|CAL)?\\s*(BEF|AFT|ABT|EXACT)?\\s*";
    private static final String PREFIX_NO_KIND = "^\\s*(EXACT|EST|CAL)?\\s*";
    private static final Pattern rangePattern = Pattern.compile(
            PREFIX_NO_KIND + "BET\\s+(?:(?:(\\d{1,2})\\s+)?(?:(" + MONTH_ABBR + ")\\s+)?)?(\\d{1,4})\\s+AND\\s+(?:(?:(\\d{1,2})\\s+)?(?:(" + MONTH_ABBR + ")\\s+)?)?(\\d{1,4})\\s*$"
    );
    private static final Pattern fullDatePattern = Pattern.compile(
            PREFIX + "(\\d{1,2})\\s+(" + MONTH_ABBR + ")\\s+(\\d{1,4})\\s*$"
    );
    private static final Pattern monthAndYearPattern = Pattern.compile(
            PREFIX + "(" + MONTH_ABBR + ")\\s+(\\d{1,4})\\s*$"
    );

    private static final Pattern yearOnlyPattern = Pattern.compile(
            PREFIX + "(\\d{1,4})\\s*$"
    );

    private static boolean nonNull(Object obj) {
        return obj != null;
    }

    public static Date parseDate(String input) {
        if (isNull(input)) {
            return null;
        }

        Date date = new Date();
        String cleanedInput = input.trim().toUpperCase();
        Matcher matcher;

        matcher = rangePattern.matcher(cleanedInput);
        if (matcher.matches()) {
            setQualityAndType(date, matcher.group(1), String.valueOf(KindType.BET));

            String day1Str = matcher.group(2);
            String month1Str = matcher.group(3);
            String year1Str = matcher.group(4);
            Integer day1 = nonNull(day1Str) ? Integer.parseInt(day1Str) : null;
            MonthType month1 = nonNull(month1Str) ? MonthType.valueOf(month1Str) : null;
            Integer year1 = nonNull(year1Str) ? Integer.parseInt(year1Str) : null;
            validateDayMonthYearComponents(day1, month1, year1);
            date.setFirstDate(createPartialDate(year1, month1, day1));

            String day2Str = matcher.group(5);
            String month2Str = matcher.group(6);
            String year2Str = matcher.group(7);
            Integer day2 = nonNull(day2Str) ? Integer.parseInt(day2Str) : null;
            MonthType month2 = nonNull(month2Str) ? MonthType.valueOf(month2Str) : null;
            Integer year2 = nonNull(year2Str) ? Integer.parseInt(year2Str) : null;
            validateDayMonthYearComponents(day2, month2, year2);
            date.setSecondDate(createPartialDate(year2, month2, day2));

            return date;
        }

        matcher = fullDatePattern.matcher(cleanedInput);
        if (matcher.matches()) {
            setQualityAndType(date, matcher.group(1), matcher.group(2));

            String dayStr = matcher.group(3);
            String monthStr = matcher.group(4);
            String yearStr = matcher.group(5);
            Integer day = Integer.parseInt(dayStr);
            MonthType month = MonthType.valueOf(monthStr);
            Integer year = Integer.parseInt(yearStr);

            validateDayMonthYearComponents(day, month, year);

            date.setFirstDate(createPartialDate(year, month, day));
            return date;
        }

        matcher = monthAndYearPattern.matcher(cleanedInput);
        if (matcher.matches()) {
            setQualityAndType(date, matcher.group(1), matcher.group(2));

            String monthStr = matcher.group(3);
            String yearStr = matcher.group(4);
            MonthType month = MonthType.valueOf(monthStr);
            Integer year = Integer.parseInt(yearStr);

            date.setFirstDate(createPartialDate(year, month, null));
            return date;
        }

        matcher = yearOnlyPattern.matcher(cleanedInput);
        if (matcher.matches()) {
            setQualityAndType(date, matcher.group(1), matcher.group(2));

            String yearStr = matcher.group(3);
            Integer year = Integer.parseInt(yearStr);

            if (yearStr.length() == 2) {
                year += 1900;
            }
            date.setFirstDate(createPartialDate(year, null, null));
            return date;
        }
        return date;
    }

    private static void validateDayMonthYearComponents(Integer day, MonthType month, Integer year) {
        if (nonNull(day)) {
            if (isNull(month) || isNull(year)) {
                throw new IllegalArgumentException("Day requires both month and year");
            }
        }
    }

    private static void setQualityAndType(Date date, String quality, String kind) {
        date.setQualityType(nonNull(quality) ? QualityType.valueOf(quality) : QualityType.EXACT);

        if (nonNull(kind)) {
            date.setKindType(KindType.valueOf(kind));
        } else {
            date.setKindType(KindType.EXACT);
        }
    }

    private static PartialDate createPartialDate(Integer year, MonthType month, Integer day) {
        PartialDate partialDate = new PartialDate();
        partialDate.setYear(year);
        partialDate.setMonth(month);
        partialDate.setDay(day);
        return partialDate;
    }
}

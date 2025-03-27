package com.ada.genealogyapp.date.validation;

import com.ada.genealogyapp.date.model.Date;
import com.ada.genealogyapp.date.model.PartialDate;
import com.ada.genealogyapp.date.type.TypeType;
import com.ada.genealogyapp.exceptions.DateException;

import java.time.LocalDate;
import java.time.Month;

import static java.util.Objects.nonNull;

public class DateValidator {

    public boolean isValid(Date date) {
        if (date.getTypeType() == TypeType.RANGE) {
            if (date.getFirstDate() == null || date.getSecondDate() == null) {
                throw new DateException("The range must have both dates");
            }
            if (!isFirstDateBeforeSecondDate(date.getFirstDate(), date.getSecondDate())) {
                throw new DateException("First date must be earliest than the second date");
            }
        }
        if (!isValidPartialDate(date.getFirstDate())) {
            throw new DateException("First date must be valid");
        }
        return date.getSecondDate() == null || isValidPartialDate(date.getSecondDate());
    }

    private boolean isValidPartialDate(PartialDate partialDate) {
        if (nonNull(partialDate.getYear()) && partialDate.getYear() < 1) {
            throw new DateException("Year must be greater than 0");
        }

        if (nonNull(partialDate.getMonth()) && (partialDate.getMonth() < 1 || partialDate.getMonth() > 12)) {
            throw new DateException("Month must be between 1 and 12");
        }

        if (nonNull(partialDate.getMonth())) {
            if (!isValidDayForMonth(partialDate.getYear(), partialDate.getMonth(), partialDate.getDay())) {
                throw new DateException("Day is not valid for the given month and year");
            }
        }
        return false;
    }

    private boolean isValidDayForMonth(Integer year, Integer month, Integer day) {
        Month monthEnum = Month.of(month);
        int maxDay = monthEnum.length(isLeapYear(year));
        return day >= 1 && day <= maxDay;
    }

    private boolean isLeapYear(Integer year) {
        return LocalDate.of(year, 1, 1).isLeapYear();
    }

    private boolean isFirstDateBeforeSecondDate(PartialDate firstDate, PartialDate secondDate) {
        if (firstDate.getYear() == null || secondDate.getYear() == null) {
            throw new DateException("Year cannot be null");
        }
        if (firstDate.getYear() > secondDate.getYear()) {
            throw new DateException("The year of the first date is later than the year of the second date");
        }
        if (firstDate.getYear().equals(secondDate.getYear())) {
            if (firstDate.getMonth() != null && secondDate.getMonth() != null) {
                if (firstDate.getMonth() > secondDate.getMonth()) {
                    throw new DateException("The month of the first date is later than the month of the second date");
                }
                if (firstDate.getMonth().equals(secondDate.getMonth())) {
                    if (firstDate.getDay() != null && secondDate.getDay() != null) {
                        return firstDate.getDay() <= secondDate.getDay();
                    } else {
                        throw new DateException("Day cannot be null");
                    }
                }
            } else {
                throw new DateException("Both dates must have a month");
            }
        }
        return true;
    }
}

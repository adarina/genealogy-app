package com.ada.genealogyapp.validation.validators;

import com.ada.genealogyapp.date.model.Date;
import com.ada.genealogyapp.date.model.PartialDate;
import com.ada.genealogyapp.date.type.KindType;
import com.ada.genealogyapp.date.type.MonthType;
import com.ada.genealogyapp.validation.result.ValidationResult;
import com.ada.genealogyapp.validation.service.FieldValidator;

import java.time.LocalDate;
import java.time.Month;
import java.util.EnumMap;
import java.util.Map;

import static com.ada.genealogyapp.date.service.DateExtractor.parseDate;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class DateValidator implements FieldValidator<String> {

    private static final Map<MonthType, Integer> MONTH_TYPE_TO_NUMBER = new EnumMap<>(MonthType.class);

    static {
        for (MonthType month : MonthType.values()) {
            MONTH_TYPE_TO_NUMBER.put(month, month.ordinal() + 1);
        }
    }

    @Override
    public void validate(String value, ValidationResult result) {
        isValid(parseDate(value), result);
    }

    public void isValid(Date date, ValidationResult result) {
        if (isNull(date)) {
            return;
        }

        if (date.getKindType() == KindType.BET) {
            if (isNull(date.getFirstDate()) || isNull(date.getSecondDate())) {
                result.addError("Both start and end dates must be provided");
            } else {
                boolean isFirstDateValid = isValidPartialDate(date.getFirstDate(), result);
                boolean isSecondDateValid = isValidPartialDate(date.getSecondDate(), result);

                if (isFirstDateValid && isSecondDateValid) {
                    if (!isFirstDateBeforeSecondDate(date.getFirstDate(), date.getSecondDate(), result)) {
                        result.addError("The first date must be before the second date");
                    }
                }
            }
        } else {
            if (nonNull(date.getSecondDate())) {
                result.addError("The second date should not be provided for this date type");
            }
            if (isNull(date.getFirstDate())) {
                result.addError("The partial date must be provided for this date type");
            } else {
                isValidPartialDate(date.getFirstDate(), result);
            }
        }
    }

    private boolean isValidPartialDate(PartialDate partialDate, ValidationResult result) {
        boolean isValid = true;

        if (isNull(partialDate)) {
            result.addError("The partial date cannot be null");
            return false;
        }

        if (nonNull(partialDate.getYear())) {
            if (partialDate.getYear() < 1) {
                result.addError("The year in the partial date must be greater than 0. Provided: " + partialDate.getYear());
                isValid = false;
            }
        }

        if (nonNull(partialDate.getMonth())) {
            if (!MONTH_TYPE_TO_NUMBER.containsKey(partialDate.getMonth())) {
                result.addError("Unknown month type in the partial date: " + partialDate.getMonth());
                isValid = false;
            }
        }

        if (nonNull(partialDate.getMonth()) && nonNull(partialDate.getDay())) {
            if (!isValidDayForMonth(partialDate.getYear(), partialDate.getMonth(), partialDate.getDay(), result)) {
                isValid = false;
            }
        } else if (nonNull(partialDate.getDay())) {
            result.addError("Day is provided without a month in the partial date");
            isValid = false;
        }

        return isValid;
    }

    private boolean isValidDayForMonth(Integer year, MonthType month, Integer day, ValidationResult result) {
        boolean isValid = true;

        if (isNull(month)) {
            result.addError("Month cannot be null when validating day");
            return false;
        }
        if (isNull(day)) {
            result.addError("Day cannot be null when month is provided");
            return false;
        }

        Integer monthNumber = MONTH_TYPE_TO_NUMBER.get(month);
        if (isNull(monthNumber)) {
            result.addError("Unknown month type in the mapping");
            return false;
        }

        Month monthEnum = Month.of(monthNumber);
        boolean isLeap = false;

        if (monthEnum == Month.FEBRUARY) {
            if (nonNull(year)) {
                isLeap = isLeapYear(year);
            } else {
                if (day < 1 || day > 29) {
                    result.addError("Day " + day + " is invalid for February");
                    isValid = false;
                }
                return isValid;
            }
        }

        int maxDay = monthEnum.length(isLeap);

        if (day < 1 || day > maxDay) {
            result.addError("Day " + day + " is invalid for the month " + month + (nonNull(year) ? " in the year " + year : ""));
            isValid = false;
        }

        return isValid;
    }

    private boolean isLeapYear(Integer year) {
        return LocalDate.of(year, 1, 1).isLeapYear();
    }

    private boolean isFirstDateBeforeSecondDate(PartialDate firstDate, PartialDate secondDate, ValidationResult result) {
        if (nonNull(firstDate.getYear()) && nonNull(secondDate.getYear())) {
            if (firstDate.getYear() > secondDate.getYear()) {
                result.addError("The first date's year cannot be after the second date's year");
                return false;
            } else if (firstDate.getYear().equals(secondDate.getYear())) {
                if (nonNull(firstDate.getMonth()) && nonNull(secondDate.getMonth())) {
                    int firstMonth = MONTH_TYPE_TO_NUMBER.get(firstDate.getMonth());
                    int secondMonth = MONTH_TYPE_TO_NUMBER.get(secondDate.getMonth());
                    if (firstMonth > secondMonth) {
                        result.addError("The first date's month cannot be after the second date's month");
                        return false;
                    } else if (firstMonth == secondMonth) {
                        if (nonNull(firstDate.getDay()) && nonNull(secondDate.getDay()) &&
                                firstDate.getDay() > secondDate.getDay()) {
                            result.addError("The first date's day cannot be after the second date's day");
                            return false;
                        }
                    }
                } else if (nonNull(firstDate.getMonth())) {
                    return true;
                } else if (nonNull(secondDate.getMonth())) {
                    result.addError("The first date must be before the second date");
                    return false;
                } else if (nonNull(firstDate.getDay()) && nonNull(secondDate.getDay()) &&
                        firstDate.getDay() > secondDate.getDay()) {
                    result.addError("The first date's day cannot be after the second date's day");
                    return false;
                }
            }
        } else if (nonNull(firstDate.getYear())) {
            result.addError("The first date must be before the second date");
            return false;
        } else if (nonNull(secondDate.getYear())) {
            return true;
        }
        return true;
    }
}

package com.ada.genealogyapp.event.validation;

import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.user.validation.ValidationResult;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Pattern;


@Slf4j
public class DescriptionEventValidator extends EventValidator {

    private static final Pattern DESCRIPTION_PATTERN = Pattern.compile("[A-Za-z0-9\\s.,'-]+");

    @Override
    public void check(Event event, ValidationResult result) {
        if (StringUtils.isBlank(event.getDescription())) {
            log.error("Event validation failed: Description is blank");
            result.addError("Description is blank");
        } else if (!DESCRIPTION_PATTERN.matcher(event.getDescription()).matches()) {
            log.error("Event validation failed: Invalid description format - " + event.getDescription());
            result.addError("Invalid description format");
        }
        checkNext(event, result);
    }
}

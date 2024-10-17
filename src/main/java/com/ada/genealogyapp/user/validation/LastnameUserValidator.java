package com.ada.genealogyapp.user.validation;

import com.ada.genealogyapp.user.model.User;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Pattern;

@Slf4j
public class LastnameUserValidator extends UserValidator {

    private static final Pattern SURNAME_PATTERN = Pattern.compile("[A-Za-z]+");

    @Override
    public void check(User user, ValidationResult result) {
        if (StringUtils.isBlank(user.getLastname())) {
            log.error("User validation failed: Last name is blank");
            result.addError("Username is blank");
        } else if (!SURNAME_PATTERN.matcher(user.getLastname()).matches()) {
            log.error("User validation failed: Invalid last name format - " + user.getLastname());
            result.addError("Invalid last name format - " + user.getLastname());
        }
        checkNext(user, result);
    }
}

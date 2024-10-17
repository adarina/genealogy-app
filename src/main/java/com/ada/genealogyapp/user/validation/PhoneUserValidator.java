package com.ada.genealogyapp.user.validation;

import com.ada.genealogyapp.user.model.User;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Pattern;

@Slf4j
public class PhoneUserValidator extends UserValidator {
    private static final Pattern PHONE_PATTERN = Pattern.compile("\\d{9}");

    @Override
    public void check(User user, ValidationResult result) {
        if (StringUtils.isBlank(user.getPhone())) {
            log.error("User validation failed: Phone is blank");
            result.addError("Phone is blank");
        } else if (!PHONE_PATTERN.matcher(user.getPhone()).matches()) {
            log.error("User validation failed: Invalid phone format - " + user.getPhone());
            result.addError("Invalid phone format - " + user.getPhone());
        }
        checkNext(user, result);
    }
}

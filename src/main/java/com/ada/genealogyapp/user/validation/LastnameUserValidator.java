package com.ada.genealogyapp.user.validation;

import com.ada.genealogyapp.user.model.User;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Pattern;

@Slf4j
public class LastnameUserValidator extends UserValidator {

    private static final Pattern SURNAME_PATTERN = Pattern.compile("[A-Za-z]+");

    @Override
    public boolean check(User user) {
        if (StringUtils.isBlank(user.getLastname())) {
            log.error("User validation failed: Last name is blank.");
            return false;
        } else if (!SURNAME_PATTERN.matcher(user.getLastname()).matches()) {
            log.error("User validation failed: Invalid last name format - " + user.getLastname());
            return false;
        }
        return checkNext(user);
    }
}

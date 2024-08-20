package com.ada.genealogyapp.user.validation;

import com.ada.genealogyapp.user.model.User;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Pattern;

@Slf4j
public class FirstnameUserValidator extends UserValidator {

    private static final Pattern NAME_PATTERN = Pattern.compile("[A-Za-z]+");

    @Override
    public boolean check(User user) {
        if (StringUtils.isBlank(user.getFirstname())) {
            log.error("User validation failed: First name is blank.");
            return false;
        } else if (!NAME_PATTERN.matcher(user.getFirstname()).matches()) {
            log.error("User validation failed: Invalid first name format - " + user.getFirstname());
            return false;
        }
        return checkNext(user);
    }
}

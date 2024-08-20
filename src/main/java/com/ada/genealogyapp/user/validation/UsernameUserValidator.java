package com.ada.genealogyapp.user.validation;

import com.ada.genealogyapp.user.model.User;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Pattern;

@Slf4j
public class UsernameUserValidator extends UserValidator {

    private static final Pattern USERNAME_PATTERN = Pattern.compile("^(.+)@(.+)$");

    @Override
    public boolean check(User user) {
        if (StringUtils.isBlank(user.getUsername())) {
            log.error("User validation failed: Username is blank.");
            return false;
        } else if (!USERNAME_PATTERN.matcher(user.getUsername()).matches()) {
            log.error("User validation failed: Invalid username format - " + user.getUsername());
            return false;
        }
        return checkNext(user);
    }
}

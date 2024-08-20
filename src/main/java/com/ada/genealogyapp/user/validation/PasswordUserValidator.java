package com.ada.genealogyapp.user.validation;

import com.ada.genealogyapp.user.model.User;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class PasswordUserValidator extends UserValidator {

    @Override
    public boolean check(User user) {
        if (StringUtils.isBlank(user.getPassword())) {
            log.error("User validation failed: Password is blank.");
            return false;
        } else if (user.getPassword().length() < 8) {
            log.error("User validation failed: Invalid password length.");
            return false;
        }
        return checkNext(user);
    }
}

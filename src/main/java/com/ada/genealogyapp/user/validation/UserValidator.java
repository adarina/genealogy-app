package com.ada.genealogyapp.user.validation;

import com.ada.genealogyapp.user.model.User;
import com.ada.genealogyapp.validation.ValidationResult;

import static java.util.Objects.nonNull;

public abstract class UserValidator {

    private UserValidator next;

    public static UserValidator link(UserValidator first, UserValidator... chain) {
        UserValidator head = first;
        for (UserValidator nextInChain : chain) {
            head.next = nextInChain;
            head = nextInChain;
        }
        return first;
    }

    public abstract void check(User user, ValidationResult result);

    protected void checkNext(User user, ValidationResult result) {
        if (nonNull(next)) {
            next.check(user, result);
        }
    }
}

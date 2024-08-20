package com.ada.genealogyapp.user.validation;

import com.ada.genealogyapp.user.model.User;

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

    public abstract boolean check(User user);

    protected boolean checkNext(User user) {
        if (next == null) {
            return true;
        }
        return next.check(user);
    }
}

package com.ada.genealogyapp.validation.model;

import com.ada.genealogyapp.validation.result.ValidationResult;

public abstract class Validator<T> {

    private Validator<T> next;

    public static <T> Validator<T> link(Validator<T> first, Validator<T>... chain) {
        Validator<T> head = first;
        for (Validator<T> nextInChain : chain) {
            head.next = nextInChain;
            head = nextInChain;
        }
        return first;
    }

    public abstract void check(T object, ValidationResult result);

    protected void checkNext(T object, ValidationResult result) {
        if (next != null) {
            next.check(object, result);
        }
    }
}

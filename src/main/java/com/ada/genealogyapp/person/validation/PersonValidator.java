package com.ada.genealogyapp.person.validation;

import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.user.validation.ValidationResult;

import static java.util.Objects.nonNull;

public abstract class PersonValidator {

    private PersonValidator next;

    public static PersonValidator link(PersonValidator first, PersonValidator... chain) {
        PersonValidator head = first;
        for (PersonValidator nextInChain : chain) {
            head.next = nextInChain;
            head = nextInChain;
        }
        return first;
    }

    public abstract void check(Person person, ValidationResult result);

    protected void checkNext(Person person, ValidationResult result) {
        if (nonNull(next)) {
            next.check(person, result);
        }
    }
}
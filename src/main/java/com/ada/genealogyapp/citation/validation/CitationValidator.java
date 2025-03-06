package com.ada.genealogyapp.citation.validation;

import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.validation.ValidationResult;

import static java.util.Objects.nonNull;

public abstract class CitationValidator {

    private CitationValidator next;

    public static CitationValidator link(CitationValidator first, CitationValidator... chain) {
        CitationValidator head = first;
        for (CitationValidator nextInChain : chain) {
            head.next = nextInChain;
            head = nextInChain;
        }
        return first;
    }

    public abstract void check(Citation citation, ValidationResult result);

    protected void checkNext(Citation citation, ValidationResult result) {
        if (nonNull(next)) {
            next.check(citation, result);
        }
    }
}
package com.ada.genealogyapp.citation.validation;

import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.user.validation.ValidationResult;

import static java.util.Objects.nonNull;

public abstract class CitationValidator {

    private com.ada.genealogyapp.citation.validation.CitationValidator next;

    public static com.ada.genealogyapp.citation.validation.CitationValidator link(com.ada.genealogyapp.citation.validation.CitationValidator first, com.ada.genealogyapp.citation.validation.CitationValidator... chain) {
        com.ada.genealogyapp.citation.validation.CitationValidator head = first;
        for (com.ada.genealogyapp.citation.validation.CitationValidator nextInChain : chain) {
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
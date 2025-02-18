package com.ada.genealogyapp.citation.validation;

import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.user.validation.ValidationResult;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Pattern;

@Slf4j
public class PageCitationValidator extends CitationValidator {

    private static final Pattern DESCRIPTION_PATTERN = Pattern.compile(".*");
    @Override
    public void check(Citation citation, ValidationResult result) {
//        if (StringUtils.isBlank(citation.getPage())) {
//            log.error("Citation validation failed: Page is blank");
//            result.addError("Page is blank");
//        } else
            if (!DESCRIPTION_PATTERN.matcher(citation.getPage()).matches()) {
            log.error("Citation validation failed: Invalid page format - " + citation.getPage());
            result.addError("Invalid page format");
        }
        checkNext(citation, result);
    }
}

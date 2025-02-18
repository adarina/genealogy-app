package com.ada.genealogyapp.person.validation;

import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.user.validation.ValidationResult;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Pattern;

@Slf4j
public class FirstnamePersonValidator extends PersonValidator {

//    private static final Pattern FIRSTNAME_PATTERN = Pattern.compile("[A-Za-z0-9\\s.,'-ąćęłńóśźżĄĆĘŁŃÓŚŹŻ]+");

    @Override
    public void check(Person person, ValidationResult result) {
//        if (StringUtils.isBlank(person.getFirstname())) {
//            log.error("Person validation failed: Firstname is blank");
//            result.addError("Firstname is blank");
//        } else if (!FIRSTNAME_PATTERN.matcher(person.getFirstname()).matches()) {
//            log.error("Person validation failed: Invalid firstname format - " + person.getFirstname());
//            result.addError("Invalid firstname format");
//        }
        if (person.getFirstname() != null) {
//            if (!FIRSTNAME_PATTERN.matcher(person.getFirstname()).matches()) {
//                log.error("Person validation failed: Invalid firstname format - " + person.getFirstname());
//                result.addError("Invalid firstname format");
//            }
            if (person.getFirstname().length() >= 100) {
                log.error("Person validation failed: Firstname is too long - " + person.getFirstname());
                result.addError("Firstname is too long");
            }
        }
        checkNext(person, result);
    }
}
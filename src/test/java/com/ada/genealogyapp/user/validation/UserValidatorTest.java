package com.ada.genealogyapp.user.validation;

import com.ada.genealogyapp.user.model.User;
import com.ada.genealogyapp.validation.ValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserValidatorTest {

    private UserValidator userValidator;

    @BeforeEach
    void setUp() {
        userValidator = UserValidator.link(
                new FirstnameUserValidator(),
                new LastnameUserValidator(),
                new UsernameUserValidator(),
                new PhoneUserValidator(),
                new PasswordUserValidator()
        );
    }

    private User createUser() {
        User user = new User();
        user.setFirstname("John");
        user.setLastname("Smith");
        user.setUsername("john.smith@email.com");
        user.setPhone("123456789");
        user.setPassword("password123");
        return user;
    }

    @Test
    void shouldFailWhenFirstnameIsNull() {
        User user = createUser();
        user.setFirstname(null);

        ValidationResult validationResult = new ValidationResult();
        userValidator.check(user, validationResult);

        assertFalse(validationResult.getErrors().isEmpty());
    }

    @Test
    void shouldFailWhenFirstnameIsEmpty() {
        User user = createUser();
        user.setFirstname("");

        ValidationResult validationResult = new ValidationResult();
        userValidator.check(user, validationResult);

        assertFalse(validationResult.getErrors().isEmpty());
    }

    @Test
    void shouldFailWhenFirstnameIsBlank() {
        User user = createUser();
        user.setFirstname(" ");

        ValidationResult validationResult = new ValidationResult();
        userValidator.check(user, validationResult);

        assertFalse(validationResult.getErrors().isEmpty());
    }

    @Test
    void shouldFailWhenFirstnameHasInvalidFormat() {
        User user = createUser();
        user.setFirstname("Marek$");

        ValidationResult validationResult = new ValidationResult();
        userValidator.check(user, validationResult);

        assertFalse(validationResult.getErrors().isEmpty());
    }

    @Test
    void shouldFailWhenLastnameIsNull() {
        User user = createUser();
        user.setLastname(null);

        ValidationResult validationResult = new ValidationResult();
        userValidator.check(user, validationResult);

        assertFalse(validationResult.getErrors().isEmpty());
    }

    @Test
    void shouldFailWhenLastnameIsEmpty() {
        User user = createUser();
        user.setLastname("");

        ValidationResult validationResult = new ValidationResult();
        userValidator.check(user, validationResult);

        assertFalse(validationResult.getErrors().isEmpty());
    }

    @Test
    void shouldFailWhenLastnameIsBlank() {
        User user = createUser();
        user.setLastname(" ");

        ValidationResult validationResult = new ValidationResult();
        userValidator.check(user, validationResult);

        assertFalse(validationResult.getErrors().isEmpty());
    }

    @Test
    void shouldFailWhenLastnameHasInvalidFormat() {
        User user = createUser();
        user.setLastname("John$");

        ValidationResult validationResult = new ValidationResult();
        userValidator.check(user, validationResult);

        assertFalse(validationResult.getErrors().isEmpty());
    }

    @Test
    void shouldFailWhenUsernameIsNull() {
        User user = createUser();
        user.setUsername(null);

        ValidationResult validationResult = new ValidationResult();
        userValidator.check(user, validationResult);

        assertFalse(validationResult.getErrors().isEmpty());
    }

    @Test
    void shouldFailWhenUsernameIsEmpty() {
        User user = createUser();
        user.setUsername("");

        ValidationResult validationResult = new ValidationResult();
        userValidator.check(user, validationResult);

        assertFalse(validationResult.getErrors().isEmpty());
    }

    @Test
    void shouldFailWhenUsernameIsBlank() {
        User user = createUser();
        user.setUsername(" ");

        ValidationResult validationResult = new ValidationResult();
        userValidator.check(user, validationResult);

        assertFalse(validationResult.getErrors().isEmpty());
    }

    @Test
    void shouldFailWhenUsernameHasInvalidFormat() {
        User user = createUser();
        user.setUsername("john");

        ValidationResult validationResult = new ValidationResult();
        userValidator.check(user, validationResult);

        assertFalse(validationResult.getErrors().isEmpty());
    }

    @Test
    void shouldPassWhenUsernameIsValid() {
        User user = createUser();
        user.setUsername("john.smith@email.com");

        ValidationResult validationResult = new ValidationResult();
        userValidator.check(user, validationResult);

        assertTrue(validationResult.getErrors().isEmpty());
    }

    @Test
    void shouldFailWhenNameContainsNonAlphabeticCharacters() {
        User user = createUser();
        user.setFirstname("John1234");

        ValidationResult validationResult = new ValidationResult();
        userValidator.check(user, validationResult);

        assertFalse(validationResult.getErrors().isEmpty());
    }

    @Test
    void shouldPassWhenNameIsValid() {
        User user = createUser();
        user.setFirstname("John");

        ValidationResult validationResult = new ValidationResult();
        userValidator.check(user, validationResult);

        assertTrue(validationResult.getErrors().isEmpty());
    }

    @Test
    void shouldFailWhenPasswordIsTooShort() {
        User user = createUser();
        user.setPassword("short");

        ValidationResult validationResult = new ValidationResult();
        userValidator.check(user, validationResult);

        assertFalse(validationResult.getErrors().isEmpty());
    }

    @Test
    void shouldPassWhenPasswordIsValid() {
        User user = createUser();
        user.setPassword("password123");

        ValidationResult validationResult = new ValidationResult();
        userValidator.check(user, validationResult);

        assertTrue(validationResult.getErrors().isEmpty());
    }

    @Test
    void shouldFailWhenPhoneIsInvalid() {
        User user = createUser();
        user.setPhone("12345abc");

        ValidationResult validationResult = new ValidationResult();
        userValidator.check(user, validationResult);

        assertFalse(validationResult.getErrors().isEmpty());
    }

    @Test
    void shouldPassWhenPhoneIsValid() {
        User user = createUser();
        user.setPhone("123456789");

        ValidationResult validationResult = new ValidationResult();
        userValidator.check(user, validationResult);

        assertTrue(validationResult.getErrors().isEmpty());
    }

    @Test
    void shouldFailWhenLastnameContainsNonAlphabeticCharacters() {
        User user = createUser();
        user.setLastname("Smith1234");

        ValidationResult validationResult = new ValidationResult();
        userValidator.check(user, validationResult);

        assertFalse(validationResult.getErrors().isEmpty());
    }

    @Test
    void shouldPassWhenSurnameIsValid() {
        User user = createUser();
        user.setLastname("Smith");

        ValidationResult validationResult = new ValidationResult();
        userValidator.check(user, validationResult);

        assertTrue(validationResult.getErrors().isEmpty());
    }
}

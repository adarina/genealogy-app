package com.ada.genealogyapp.validation;

import com.ada.genealogyapp.user.model.User;
import com.ada.genealogyapp.user.validation.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserValidatorTest {

    private UserValidator userValidator;

    private User user;

    @BeforeEach
    void setUp() {
        userValidator = UserValidator.link(
                new FirstnameUserValidator(),
                new LastnameUserValidator(),
                new UsernameUserValidator(),
                new PhoneUserValidator(),
                new PasswordUserValidator()
        );

        user = new User();
        user.setFirstname("Marek");
        user.setLastname("Smith");
        user.setUsername("marek@m.com");
        user.setPhone("123456789");
        user.setPassword("password123");
    }

    @Test
    void shouldFailWhenFirstnameIsNull() {
        user.setFirstname(null);

        assertFalse(userValidator.check(user));
    }

    @Test
    void shouldFailWhenFirstnameIsEmpty() {
        user.setFirstname("");

        assertFalse(userValidator.check(user));
    }

    @Test
    void shouldFailWhenFirstnameIsBlank() {
        user.setFirstname(" ");

        assertFalse(userValidator.check(user));
    }

    @Test
    void shouldFailWhenFirstnameHasInvalidFormat() {
        user.setFirstname("Marek$");

        assertFalse(userValidator.check(user));
    }

    @Test
    void shouldFailWhenLastnameIsNull() {
        user.setLastname(null);

        assertFalse(userValidator.check(user));
    }

    @Test
    void shouldFailWhenLastnameIsEmpty() {
        user.setLastname("");

        assertFalse(userValidator.check(user));
    }

    @Test
    void shouldFailWhenLastnameIsBlank() {
        user.setLastname(" ");

        assertFalse(userValidator.check(user));
    }

    @Test
    void shouldFailWhenLastnameHasInvalidFormat() {
        user.setLastname("Marek$");

        assertFalse(userValidator.check(user));
    }

    @Test
    void shouldFailWhenUsernameIsNull() {
        user.setUsername(null);

        assertFalse(userValidator.check(user));
    }

    @Test
    void shouldFailWhenUsernameIsEmpty() {
        user.setUsername("");

        assertFalse(userValidator.check(user));
    }

    @Test
    void shouldFailWhenUsernameIsBlank() {
        user.setUsername(" ");

        assertFalse(userValidator.check(user));
    }

    @Test
    void shouldFailWhenUsernameHasInvalidFormat() {
        user.setUsername("marek");

        assertFalse(userValidator.check(user));
    }

    @Test
    void shouldPassWhenUsernameIsValid() {
        user.setUsername("mareksmith@onet.com");

        assertTrue(userValidator.check(user));
    }

    @Test
    void shouldFailWhenNameContainsNonAlphabeticCharacters() {
        user.setFirstname("marek1234");

        assertFalse(userValidator.check(user));
    }

    @Test
    void shouldPassWhenNameIsValid() {
        user.setFirstname("Marek");

        assertTrue(userValidator.check(user));
    }

    @Test
    void shouldFailWhenPasswordIsTooShort() {
        user.setPassword("short");

        assertFalse(userValidator.check(user));
    }

    @Test
    void shouldPassWhenPasswordIsValid() {
        user.setPassword("password123");

        assertTrue(userValidator.check(user));
    }

    @Test
    void shouldFailWhenPhoneIsInvalid() {
        user.setPhone("12345abc");

        assertFalse(userValidator.check(user));
    }

    @Test
    void shouldPassWhenPhoneIsValid() {
        user.setPhone("123456789");

        assertTrue(userValidator.check(user));
    }

    @Test
    void shouldFailWhenLastnameContainsNonAlphabeticCharacters() {
        user.setLastname("Smith1234");

        assertFalse(userValidator.check(user));
    }

    @Test
    void shouldPassWhenSurnameIsValid() {
        user.setLastname("Smith");

        assertTrue(userValidator.check(user));
    }
}

package com.ada.genealogyapp.user.validation;

import com.ada.genealogyapp.user.model.User;
import com.ada.genealogyapp.user.validation.*;
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

        assertFalse(userValidator.check(user));
    }

    @Test
    void shouldFailWhenFirstnameIsEmpty() {
        User user = createUser();
        user.setFirstname("");

        assertFalse(userValidator.check(user));
    }

    @Test
    void shouldFailWhenFirstnameIsBlank() {
        User user = createUser();
        user.setFirstname(" ");

        assertFalse(userValidator.check(user));
    }

    @Test
    void shouldFailWhenFirstnameHasInvalidFormat() {
        User user = createUser();
        user.setFirstname("Marek$");

        assertFalse(userValidator.check(user));
    }

    @Test
    void shouldFailWhenLastnameIsNull() {
        User user = createUser();
        user.setLastname(null);

        assertFalse(userValidator.check(user));
    }

    @Test
    void shouldFailWhenLastnameIsEmpty() {
        User user = createUser();
        user.setLastname("");

        assertFalse(userValidator.check(user));
    }

    @Test
    void shouldFailWhenLastnameIsBlank() {
        User user = createUser();
        user.setLastname(" ");

        assertFalse(userValidator.check(user));
    }

    @Test
    void shouldFailWhenLastnameHasInvalidFormat() {
        User user = createUser();
        user.setLastname("John$");

        assertFalse(userValidator.check(user));
    }

    @Test
    void shouldFailWhenUsernameIsNull() {
        User user = createUser();
        user.setUsername(null);

        assertFalse(userValidator.check(user));
    }

    @Test
    void shouldFailWhenUsernameIsEmpty() {
        User user = createUser();
        user.setUsername("");

        assertFalse(userValidator.check(user));
    }

    @Test
    void shouldFailWhenUsernameIsBlank() {
        User user = createUser();
        user.setUsername(" ");

        assertFalse(userValidator.check(user));
    }

    @Test
    void shouldFailWhenUsernameHasInvalidFormat() {
        User user = createUser();
        user.setUsername("john");

        assertFalse(userValidator.check(user));
    }

    @Test
    void shouldPassWhenUsernameIsValid() {
        User user = createUser();
        user.setUsername("john.smith@email.com");

        assertTrue(userValidator.check(user));
    }

    @Test
    void shouldFailWhenNameContainsNonAlphabeticCharacters() {
        User user = createUser();
        user.setFirstname("John1234");

        assertFalse(userValidator.check(user));
    }

    @Test
    void shouldPassWhenNameIsValid() {
        User user = createUser();
        user.setFirstname("John");

        assertTrue(userValidator.check(user));
    }

    @Test
    void shouldFailWhenPasswordIsTooShort() {
        User user = createUser();
        user.setPassword("short");

        assertFalse(userValidator.check(user));
    }

    @Test
    void shouldPassWhenPasswordIsValid() {
        User user = createUser();
        user.setPassword("password123");

        assertTrue(userValidator.check(user));
    }

    @Test
    void shouldFailWhenPhoneIsInvalid() {
        User user = createUser();
        user.setPhone("12345abc");

        assertFalse(userValidator.check(user));
    }

    @Test
    void shouldPassWhenPhoneIsValid() {
        User user = createUser();
        user.setPhone("123456789");

        assertTrue(userValidator.check(user));
    }

    @Test
    void shouldFailWhenLastnameContainsNonAlphabeticCharacters() {
        User user = createUser();
        user.setLastname("Smith1234");

        assertFalse(userValidator.check(user));
    }

    @Test
    void shouldPassWhenSurnameIsValid() {
        User user = createUser();
        user.setLastname("Smith");

        assertTrue(userValidator.check(user));
    }
}

package com.ada.genealogyapp.user.validation;

import com.ada.genealogyapp.exceptions.ValidationException;
import com.ada.genealogyapp.user.model.User;
import com.ada.genealogyapp.user.service.UserValidationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserValidatorTest {

    @InjectMocks
    UserValidationService userValidationService;


    User createUser() {
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

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userValidationService.validateUser(user)
        );
        assertTrue(exception.getMessage().contains("Value is null"));
    }

    @Test
    void shouldFailWhenFirstnameIsEmpty() {
        User user = createUser();
        user.setFirstname("");

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userValidationService.validateUser(user)
        );
        assertTrue(exception.getMessage().contains("Value is blank"));
    }

    @Test
    void shouldFailWhenFirstnameIsBlank() {
        User user = createUser();
        user.setFirstname(" ");

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userValidationService.validateUser(user)
        );
        assertTrue(exception.getMessage().contains("Value is blank"));
    }

    @Test
    void shouldFailWhenLastnameIsNull() {
        User user = createUser();
        user.setLastname(null);

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userValidationService.validateUser(user)
        );
        assertTrue(exception.getMessage().contains("Value is null"));
    }

    @Test
    void shouldFailWhenLastnameIsEmpty() {
        User user = createUser();
        user.setLastname("");

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userValidationService.validateUser(user)
        );
        assertTrue(exception.getMessage().contains("Value is blank"));
    }

    @Test
    void shouldFailWhenLastnameIsBlank() {
        User user = createUser();
        user.setLastname(" ");

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userValidationService.validateUser(user)
        );
        assertTrue(exception.getMessage().contains("Value is blank"));
    }

    @Test
    void shouldFailWhenUsernameIsNull() {
        User user = createUser();
        user.setUsername(null);

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userValidationService.validateUser(user)
        );
        assertTrue(exception.getMessage().contains("Value is null"));
    }

    @Test
    void shouldFailWhenUsernameIsEmpty() {
        User user = createUser();
        user.setUsername("");

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userValidationService.validateUser(user)
        );
        assertTrue(exception.getMessage().contains("Value is blank"));
        assertTrue(exception.getMessage().contains("is not email"));
    }

    @Test
    void shouldFailWhenUsernameIsBlank() {
        User user = createUser();
        user.setUsername(" ");

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userValidationService.validateUser(user)
        );
        assertTrue(exception.getMessage().contains("Value is blank"));
        assertTrue(exception.getMessage().contains("is not email"));
    }

    @Test
    void shouldFailWhenUsernameHasInvalidFormat() {
        User user = createUser();
        user.setUsername("john");

        ValidationException exception = assertThrows(
                ValidationException.class, () -> userValidationService.validateUser(user));

        assertTrue(exception.getMessage().contains("is not email"));
    }

    @Test
    void shouldPassWhenUsernameIsValid() {
        User user = createUser();
        user.setUsername("john.smith@email.com");

        assertDoesNotThrow(() -> userValidationService.validateUser(user));
    }

    @Test
    void shouldFailWhenNameIsTooLong() {
        User user = createUser();
        user.setFirstname("John12345678901234567891023456789101234567809985983456345345345345");

        ValidationException exception = assertThrows(
                ValidationException.class, () -> userValidationService.validateUser(user));

        assertTrue(exception.getMessage().contains("exceeds the maximum length of 50"));
    }

    @Test
    void shouldPassWhenNameIsValid() {
        User user = createUser();
        user.setFirstname("John");

        assertDoesNotThrow(() -> userValidationService.validateUser(user));
    }

    @Test
    void shouldFailWhenPasswordIsTooShort() {
        User user = createUser();
        user.setPassword("short");

        ValidationException exception = assertThrows(
                ValidationException.class, () -> userValidationService.validateUser(user));

        assertTrue(exception.getMessage().contains("is shorter than the minimum length of"));
    }

    @Test
    void shouldPassWhenPasswordIsValid() {
        User user = createUser();
        user.setPassword("password123");

        assertDoesNotThrow(() -> userValidationService.validateUser(user));
    }

    @Test
    void shouldFailWhenPhoneIsInvalid() {
        User user = createUser();
        user.setPhone("12345abc");

        ValidationException exception = assertThrows(
                ValidationException.class, () -> userValidationService.validateUser(user));

        assertTrue(exception.getMessage().contains("is not numeric"));
    }

    @Test
    void shouldPassWhenPhoneIsValid() {
        User user = createUser();
        user.setPhone("123456789");

        assertDoesNotThrow(() -> userValidationService.validateUser(user));
    }

    @Test
    void shouldFailWhenLastnameIsTooLong() {
        User user = createUser();
        user.setLastname("Smith12345678901234567891023456789101234567809985983456345345345345");

        ValidationException exception = assertThrows(
                ValidationException.class, () -> userValidationService.validateUser(user));

        assertTrue(exception.getMessage().contains("exceeds the maximum length of 50"));
    }

    @Test
    void shouldPassWhenSurnameIsValid() {
        User user = createUser();
        user.setLastname("Smith");

        assertDoesNotThrow(() -> userValidationService.validateUser(user));
    }
}

package com.ada.genealogyapp.passwords;

import com.ada.genealogyapp.exceptions.InvalidCredentialsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class DefaultPasswordService implements PasswordService {

    private final PasswordEncoder passwordEncoder;
    @Override
    public void match(String inputPassword, String alreadyExistPassword) {
        boolean passwordNotMatch = !passwordEncoder.matches(inputPassword, alreadyExistPassword);

        if (passwordNotMatch) {
            log.error("Invalid username or password for username: {}", inputPassword);
            throw new InvalidCredentialsException("Invalid username or password for username: " + inputPassword);
        }
    }
}

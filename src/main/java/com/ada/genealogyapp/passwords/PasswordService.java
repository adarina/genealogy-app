package com.ada.genealogyapp.passwords;

public interface PasswordService {

    void match(String inputPassword, String alreadyExistPassword);
}

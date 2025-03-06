package com.ada.genealogyapp.auth;

import com.ada.genealogyapp.user.dto.UserLoginRequest;
import com.ada.genealogyapp.user.dto.UserLoginResponse;

public interface AuthService {

    UserLoginResponse login(UserLoginRequest request);
}

package com.inspire.auth.service;

import com.inspire.auth.domain.dto.request.UserLoginDTO;
import com.inspire.auth.domain.dto.response.AccessTokenDTO;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

    AccessTokenDTO login(HttpServletResponse res, UserLoginDTO userLoginDTO);
    AccessTokenDTO reissue(HttpServletResponse response, String refreshToken);
    void logout(HttpServletResponse res, Long userId, String refreshToken);
}

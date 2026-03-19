package com.inspire.auth.service;

import com.inspire.auth.domain.dto.response.AccessTokenDTO;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

    AccessTokenDTO reissue(HttpServletResponse response, String refreshToken);
}

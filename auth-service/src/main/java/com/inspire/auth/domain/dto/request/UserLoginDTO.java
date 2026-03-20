package com.inspire.auth.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Schema(description = "로그인 요청 데이터")
public class UserLoginDTO {
    @Schema(description = "사용자 로그인 아이디", example = "test@example.com")
    @Email
    private String email;

    @Schema(description = "사용자 비밀번호", example = "test012345!")
    /*
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "비밀번호는 영문자, 숫자, 특수문자를 포함한 8자 이상이어야 합니다")
     */
    private String password;
}

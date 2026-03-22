package com.inspire.user.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Schema(description = "토큰 응답 데이터 (Access Token 및 만료 시간)")
public class UserResponse {
    @Schema(description = "사용자 고유 ID", example = "1")
    private Long userId;
    @Schema(description = "사용자 이름", example = "김철수")
    private String name;
    @Schema(description = "사용자 전화번호", example = "010-1234-5678")
    private String phone;
    @Schema(description = "사용자 이메일", example = "johndoe@example.com")
    private String email;

    @Builder
    public UserResponse(Long userId, String name, String phone, String email) {
        this.userId = userId;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }
}

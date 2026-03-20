package com.inspire.user.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {
    
    // Auth 연동 전이므로 프로필 수정은 일단 이름(name)만
    private String name;

}
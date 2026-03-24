package com.inspire.user.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.inspire.common.binding.Update;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class UserUpdateRequest {

    Update<@NotBlank String> name = Update.absent();
    Update<@NotBlank String> phone = Update.absent();
    Update<@NotBlank String> email = Update.absent();
}

package com.sparta.kidscafe.api.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record SigninRequestDto() {
    @Email
    @NotNull(message = "")
    String email;

    @NotNull(message = "")
    String paasword;

}

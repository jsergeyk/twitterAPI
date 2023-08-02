package com.proxyseller.twitter.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record SignupDTO(@NotBlank
                        @Size(min=3, max = 30)
                        String username,
                        @Email
                        @NotBlank
                        @Size(max = 60)
                        String email,
                        @NotBlank
                        @Size(min = 6, max = 60)
                        String password,
                        Boolean isActive) {
}

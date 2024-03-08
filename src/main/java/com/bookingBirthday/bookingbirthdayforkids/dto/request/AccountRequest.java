package com.bookingBirthday.bookingbirthdayforkids.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class AccountRequest {
    @NotBlank(message = "Username can not blank")
    private String username;
    @NotBlank(message = "Password can not blank")
    private String password;
    @NotBlank(message = "FullName can not blank")
    private String fullName;
    @NotBlank(message = "Email can not blank")
    private String email;
    @NotBlank(message = "Phone can not blank")
    private String phone;
    private LocalDateTime dob;
}

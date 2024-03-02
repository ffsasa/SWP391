package com.bookingBirthday.bookingbirthdayforkids.dto.request;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PartyBookingRequest {
    @NotBlank(message = "Kid Name cannot blank")
    private String kidName;
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate kidDOB;
    @NotBlank(message = "Email cannot blank")
    @Email(message = "Invalid email format")
    private String email;
    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(regexp = "\\d{10}", message = "Invalid phone number format")
    private int phone;
}

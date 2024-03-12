package com.bookingBirthday.bookingbirthdayforkids.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Map;

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
    private String phone;

    @NotNull(message = "Theme ID cannot be null")
    private Long themeId;
    @NotNull(message = "Package ID cannot be null")
    private Long packageId;
    @NotNull(message = "Slot in venue ID cannot be null")
    private Long slotInVenueId;
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate Date;

    private Map<String, Integer> dataUpgrade;
}

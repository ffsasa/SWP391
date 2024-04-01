package com.bookingBirthday.bookingbirthdayforkids.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PartyBookingRequest {
    @NotBlank(message = "Kid Name cannot blank")
    private String kidName;
    @NotBlank(message = "Reservation Name cannot blank")
    private String reservationAgent;
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate kidDOB;
    @NotBlank(message = "Email cannot blank")
    @Email(message = "Invalid email format")
    private String email;
    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(regexp = "\\d{10}", message = "Invalid phone number format")
    private String phone;
    @Min(value = 1, message = "Capacity value must be greater than or equal to 1")
    private int participantAmount;

    @NotNull(message = "Package Decoration ID cannot be null")
    private Long packageDecoId;
    @NotNull(message = "Package Food ID cannot be null")
    private Long packageFoodId;
    @NotNull(message = "Slot in room ID cannot be null")
    private Long slotInRoomId;
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate Date;

    private List<UpgradeServiceRequest> dataUpgrade;
}

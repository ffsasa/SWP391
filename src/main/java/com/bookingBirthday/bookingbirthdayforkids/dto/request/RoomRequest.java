package com.bookingBirthday.bookingbirthdayforkids.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomRequest {
    @NotBlank(message = "Room Name cannot blank")
    private String roomName;
    private Long venueId;
    private String roomImgUrl;
    @Min(value = 20, message = "Capacity value must be greater than or equal to 20")
    private int capacity;
    @Min(value = 0, message = "Pricing value must be greater than or equal to 0")
    private float pricing;
}

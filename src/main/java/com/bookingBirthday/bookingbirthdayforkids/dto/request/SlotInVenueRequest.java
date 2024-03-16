package com.bookingBirthday.bookingbirthdayforkids.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SlotInVenueRequest {
    @NotNull(message = "Venue ID cannot be null")
    private Long venue_id;
    @NotNull(message = "Slot ID cannot be null")
    private Long slot_id;
}

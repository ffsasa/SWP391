package com.bookingBirthday.bookingbirthdayforkids.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SlotInVenueRequest {
    private Long venue_id;
    private Long slot_id;
}

package com.bookingBirthday.bookingbirthdayforkids.dto.request;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PartyDatedRequest {
    private long slotId;
    private long venueId;
    private LocalDateTime date;
}
package com.bookingBirthday.bookingbirthdayforkids.dto.request;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PartyDatedRequest {
    private String slotId;
    private String venueId;
    private LocalDateTime date;
}
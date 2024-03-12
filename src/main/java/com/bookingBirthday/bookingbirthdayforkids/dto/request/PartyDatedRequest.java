package com.bookingBirthday.bookingbirthdayforkids.dto.request;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PartyDatedRequest {
    private Long slotInVenueId;

    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate Date;
}
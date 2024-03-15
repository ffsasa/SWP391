package com.bookingBirthday.bookingbirthdayforkids.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PackageInVenueRequest {
    private Long packageId;

    private Long venueId;
}

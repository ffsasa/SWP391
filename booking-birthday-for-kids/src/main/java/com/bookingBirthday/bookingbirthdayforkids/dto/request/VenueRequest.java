package com.bookingBirthday.bookingbirthdayforkids.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VenueRequest {
    public String venueName;
    public String venueDescription;
    public String venueImgUrl;
    public String location;
    public int capacity;
}

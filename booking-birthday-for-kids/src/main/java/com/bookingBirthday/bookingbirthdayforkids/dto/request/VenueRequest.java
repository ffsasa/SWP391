package com.bookingBirthday.bookingbirthdayforkids.dto.request;


import lombok.*;



@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VenueRequest {
    private String venueName;
    private String venueDescription;
    private String venueImgUrl;
    private String location;
    private String capacity;
}

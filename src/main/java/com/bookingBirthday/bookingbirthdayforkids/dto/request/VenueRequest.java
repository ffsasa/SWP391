package com.bookingBirthday.bookingbirthdayforkids.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VenueRequest {
    @NotBlank(message = "Venue name cannot be blank")
    public String venueName;
    @NotBlank(message = "Description cannot be blank")
    public String venueDescription;
    public String venueImgUrl;
    @NotBlank(message = "Location cannot be blank")
    public String location;
    @Min(value = 1, message = "Capacity value must be greater than or equal to 1")
    public int capacity;
}

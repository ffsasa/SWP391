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
public class ThemeInVenueRequest {
    @NotNull(message = "Theme ID cannot be null")
    private Long themeId;
    @NotNull(message = "Venue ID cannot be null")
    private Long venueId;
}

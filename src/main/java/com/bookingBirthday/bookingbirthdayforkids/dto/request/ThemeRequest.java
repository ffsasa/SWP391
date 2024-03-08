package com.bookingBirthday.bookingbirthdayforkids.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ThemeRequest {
    @NotBlank(message = "Theme name cannot be blank")
    private String themeName;
    @NotBlank(message = "Description cannot be blank")
    private String themDescription;
    private String themeImgUrl;
}

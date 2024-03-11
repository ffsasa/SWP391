package com.bookingBirthday.bookingbirthdayforkids.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SlotRequest {

    @NotNull(message = "TimeStart value cannot be null")
    @NotBlank(message = "TimeStart value cannot be blank")
    @DateTimeFormat(pattern = "hh-mm-ss")
    private String timeStart;

    @NotNull(message = "TimeEnd value cannot be null")
    @NotBlank(message = "TimeEnd value cannot be blank")
    @DateTimeFormat(pattern = "hh-mm-ss")
    private String timeEnd;
}

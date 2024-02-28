package com.bookingBirthday.bookingbirthdayforkids.dto.request;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SlotRequest {
    private LocalDateTime timeStart;
    private LocalDateTime timeEnd;
}

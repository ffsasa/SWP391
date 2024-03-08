package com.bookingBirthday.bookingbirthdayforkids.dto.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Data
public class ResponseObj {
    private String status;
    private String message;
    private Object data;
}

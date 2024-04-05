package com.bookingBirthday.bookingbirthdayforkids.dto.response;

import com.bookingBirthday.bookingbirthdayforkids.model.Meta;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Data
public class ResponseObjMeta {
    private String status;
    private String message;
    private Object data;
    private Meta metadata;
}

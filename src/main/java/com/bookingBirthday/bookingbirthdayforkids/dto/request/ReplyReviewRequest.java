package com.bookingBirthday.bookingbirthdayforkids.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReplyReviewRequest {
    @NotBlank(message = "Reply cannot be blank")
    private String replyMessage;

}

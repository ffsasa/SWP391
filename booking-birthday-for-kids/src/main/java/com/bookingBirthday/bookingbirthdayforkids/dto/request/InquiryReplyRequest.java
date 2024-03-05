package com.bookingBirthday.bookingbirthdayforkids.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InquiryReplyRequest {
    @NotBlank(message = "Reply cannot be blank")
    private String inquiryQuestion;
}

package com.bookingBirthday.bookingbirthdayforkids.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InquiryQuestionRequest {
    @NotBlank(message = "Question cannot be blank")
    private String inquiryQuestion;
}

package com.bookingBirthday.bookingbirthdayforkids.dto.request;

import com.bookingBirthday.bookingbirthdayforkids.model.InquiryStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InquiryReplyRequest {
    @NotBlank(message = "Reply cannot be blank")
    private String inquiryReply;
    @Enumerated(EnumType.STRING)
    InquiryStatus inquiryStatus;
}

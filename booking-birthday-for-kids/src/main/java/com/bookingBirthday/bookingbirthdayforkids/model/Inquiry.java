package com.bookingBirthday.bookingbirthdayforkids.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Inquiry extends BaseEntity{
    @NotBlank(message = "Question cannot be blank")
    private String inquiryQuestion;
    @NotBlank(message = "Reply cannot be blank")
    private String inquiryReply;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
}

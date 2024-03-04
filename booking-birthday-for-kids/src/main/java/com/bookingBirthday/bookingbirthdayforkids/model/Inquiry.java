package com.bookingBirthday.bookingbirthdayforkids.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @JsonBackReference
    private Account account;

    @ManyToOne
    @JoinColumn(name = "accountReply_id")
    @JsonBackReference
    private Account accountReply;

}

package com.bookingBirthday.bookingbirthdayforkids.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Inquiry extends BaseEntity{

    private String inquiryQuestion;
    private String inquiryReply;
    @Enumerated(EnumType.STRING)
    private InquiryStatus status;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "accountReply_id")
    private Account accountReply;

    public void setReplyAndStatus(String reply, InquiryStatus newStatus) {
        if (reply != null && (newStatus == InquiryStatus.APPROVED || newStatus == InquiryStatus.REJECTED)) {
            this.inquiryReply = reply;
            this.status = newStatus;
        } else {
            throw new IllegalArgumentException("Invalid status for reply.");
        }
    }
}

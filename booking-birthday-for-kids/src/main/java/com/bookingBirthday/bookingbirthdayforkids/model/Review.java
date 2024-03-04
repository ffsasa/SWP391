package com.bookingBirthday.bookingbirthdayforkids.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Review extends BaseEntity{
    @NotBlank(message = "Message cannot be blank")
    private String reviewMessage;
    @NotBlank(message = "Reply message cannot be blank")
    private String replyMessage;
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private float rating;
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    @JsonIgnore
    private LocalDateTime reviewDate;

    @ManyToOne
    @JoinColumn(name = "account_id")
    @JsonBackReference
    private Account account;

    @ManyToOne
    @JoinColumn(name = "partyBooking_id")
    @JsonBackReference
    private PartyBooking partyBooking;
}

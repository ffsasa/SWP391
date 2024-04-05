package com.bookingBirthday.bookingbirthdayforkids.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Review extends BaseEntity{

    private String reviewMessage;
    private String replyMessage;
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private float rating;
    @OneToOne
    @JoinColumn(name = "partyBooking_id", referencedColumnName = "id")
    @JsonIgnore
    private PartyBooking partyBooking;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "venue_id")
    private Venue venue;

    @Transient
    @JsonProperty("partyBookingId")
    private Long partyBookingId;

    @Transient
    @JsonProperty("account")
    private Account account;

    @Transient
    @JsonProperty("accountReply")
    private Account accountReply;
}

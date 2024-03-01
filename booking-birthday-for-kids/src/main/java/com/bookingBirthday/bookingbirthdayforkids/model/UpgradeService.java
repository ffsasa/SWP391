package com.bookingBirthday.bookingbirthdayforkids.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UpgradeService extends BaseEntity{
    @Min(value = 1, message = "Count value must be greater than or equal to 1")
    private int count;
    @Min(value = 0, message = "Pricing value must be greater than or equal to 0")
    private float pricing;

    @ManyToOne
    @JoinColumn(name = "party_booking_id")
    @JsonBackReference
    private PartyBooking partyBooking;
}

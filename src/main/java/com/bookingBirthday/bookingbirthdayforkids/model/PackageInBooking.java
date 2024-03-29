package com.bookingBirthday.bookingbirthdayforkids.model;


import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PackageInBooking extends BaseEntity{

    @ManyToOne()
    @JoinColumn(name = "partyBooking_id")
    private PartyBooking partyBooking;

    @ManyToOne()
    @JoinColumn(name = "packageInVenue_id")
    private PackageInVenue packageInVenue;
}

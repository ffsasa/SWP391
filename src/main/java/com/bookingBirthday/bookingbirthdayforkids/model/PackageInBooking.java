package com.bookingBirthday.bookingbirthdayforkids.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class PackageInBooking extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "partyBooking_id")
    @JsonIgnore
    private PartyBooking partyBooking;

    @ManyToOne
    @JoinColumn(name = "package_id")
    private Package aPackage;
}

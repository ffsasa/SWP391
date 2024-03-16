package com.bookingBirthday.bookingbirthdayforkids.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class PackageInVenue extends BaseEntity{
    @ManyToOne
    @JoinColumn(name = "venue_id")
    @JsonBackReference
    private Venue venue;

    @ManyToOne
    @JoinColumn(name = "package_id")
    @JsonBackReference
    private Package apackage;

    @OneToMany(mappedBy = "packageInVenue")
    @JsonManagedReference
    @JsonIgnore
    private List<PartyBooking> partyBookingList;

    @Transient
    @JsonProperty("packageObject")
    private Package packageObject;
}

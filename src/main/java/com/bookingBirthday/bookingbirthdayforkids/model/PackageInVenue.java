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
    @JsonIgnore
    private Venue venue;

    @ManyToOne
    @JoinColumn(name = "package_id")
    private Package apackage;

    @OneToMany(mappedBy = "packageInVenue", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<PackageInBooking> packageInBooking;

    @OneToMany(mappedBy = "packageInVenue")
    @JsonIgnore
    private List<PackageInBooking> packageInBookings;

}

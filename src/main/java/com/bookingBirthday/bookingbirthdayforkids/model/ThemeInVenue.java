package com.bookingBirthday.bookingbirthdayforkids.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class ThemeInVenue extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "venue_id")
    @JsonBackReference
    private Venue venue;

    @ManyToOne
    @JoinColumn(name = "theme_id")
    @JsonBackReference
    private Theme theme;

    @OneToMany(mappedBy = "themeInVenue")
    @JsonManagedReference
    @JsonIgnore
    private List<PartyBooking> partyBookingList;

}

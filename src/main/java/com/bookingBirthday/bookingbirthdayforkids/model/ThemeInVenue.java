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
public class ThemeInVenue extends BaseEntity {

    @Transient
    @JsonProperty("themeObject")
    private Theme themeObject;

    @ManyToOne
    @JoinColumn(name = "venue_id")
    @JsonBackReference
    private Venue venue;

    @ManyToOne
    @JoinColumn(name = "theme_id")
    @JsonBackReference
    private Theme theme;

    @OneToMany(mappedBy = "themeInVenue", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    @JsonIgnore
    private List<PartyBooking> partyBookingList;
}

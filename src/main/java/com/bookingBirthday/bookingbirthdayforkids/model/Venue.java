package com.bookingBirthday.bookingbirthdayforkids.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Venue extends BaseEntity{
    @NotBlank(message = "Venue name cannot be blank")
    private String venueName;
    @Column(name = "venue_description",columnDefinition = "TEXT")
    @NotBlank(message = "Description cannot be blank")
    private String venueDescription;
    @Column(name = "venue_img_url",columnDefinition = "TEXT")
    private String venueImgUrl;
    @NotBlank(message = "Location cannot be blank")
    private String location;
    @Min(value = 1, message = "Capacity value must be greater than or equal to 1")
    private int capacity;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("slot_not_added")
    private List<Slot> slotNotAddedList;

    @ManyToMany
    @JsonIgnore
    @JoinTable(
            name = "theme_venue",
            joinColumns = @JoinColumn(name = "venue_id"),
            inverseJoinColumns = @JoinColumn(name = "theme_id")
    )
    Set<Theme> themeSet;

    @ManyToMany
    @JsonIgnore
    @JoinTable(
            name = "package_venue",
            joinColumns = @JoinColumn(name = "venue_id"),
            inverseJoinColumns = @JoinColumn(name = "package_id")
    )
    Set<Package> packageSet;

    @OneToMany(mappedBy = "venue")
    @JsonManagedReference
    @JsonIgnore
    private List<PartyBooking> partyBookingList;

    @OneToMany(mappedBy = "venue")
    @JsonManagedReference
    private List<SlotInVenue> slotInVenueList;
}

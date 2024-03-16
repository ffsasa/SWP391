package com.bookingBirthday.bookingbirthdayforkids.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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


    @OneToMany(mappedBy = "venue")
    @JsonManagedReference
    private List<PackageInVenue> packageInVenueList;

    @OneToMany(mappedBy = "venue")
    @JsonManagedReference
    private List<ThemeInVenue> themeInVenueList;

    @OneToMany(mappedBy = "venue")
    @JsonManagedReference
    private List<SlotInVenue> slotInVenueList;
}

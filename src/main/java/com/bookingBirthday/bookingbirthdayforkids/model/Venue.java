package com.bookingBirthday.bookingbirthdayforkids.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "Street cannot be blank")
    private String street;
    @NotBlank(message = "Ward cannot be blank")
    private String ward;
    @NotBlank(message = "District cannot be blank")
    private String district;
    @NotBlank(message = "City cannot be blank")
    private String city;

    @OneToMany(mappedBy = "venue")
    @JsonIgnore
    private List<PackageInVenue> packageInVenueList;
    
    @OneToMany(mappedBy = "venue")
    @JsonIgnore
    private List<Room> roomList;
}

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
    private String venueName;
    @Column(name = "venue_description",columnDefinition = "TEXT")
    private String venueDescription;
    @Column(name = "venue_img_url",columnDefinition = "TEXT")
    private String venueImgUrl;
    private String street;
    private String ward;
    private String district;
    private String city;

    @OneToMany(mappedBy = "venue")
    @JsonIgnore
    private List<PackageInVenue> packageInVenueList;
    
    @OneToMany(mappedBy = "venue")
//    @JsonIgnore
    private List<Room> roomList;
    @OneToOne
    @JoinColumn(name = "account_id")
    Account account;
}

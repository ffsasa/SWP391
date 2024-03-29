package com.bookingBirthday.bookingbirthdayforkids.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
//    @JsonIgnore
    private List<Room> roomList;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

    @OneToMany(mappedBy = "venue")
    private List<Package> packageList;
}

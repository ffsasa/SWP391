package com.bookingBirthday.bookingbirthdayforkids.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Venue extends BaseEntity {
    @NotBlank(message = "Venue cannot be blank")
    private String venueName;
    @NotBlank(message = "Description cannot be blank")
    private String venueDescription;
    private String venueImgUrl;
    @NotBlank(message = "Location cannot be blank")
    private String location;
    private int capacity;

//    @OneToMany (mappedBy = "venue")
//    @JsonManagedReference
//    private List<PartyDated> partyDatedList;
}
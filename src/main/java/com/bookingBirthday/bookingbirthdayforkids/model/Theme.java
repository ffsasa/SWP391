package com.bookingBirthday.bookingbirthdayforkids.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
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
public class Theme extends BaseEntity{
    @NotBlank(message = "Theme name cannot be blank")
    private String themeName;
    @NotBlank(message = "Description cannot be blank")
    private String themDescription;
    @Column(name = "avatar_url",columnDefinition = "TEXT")
    private String themeImgUrl;

    @ManyToMany(mappedBy = "themeSet")
    @JsonIgnore
    Set<Venue> venueSet;

    @OneToMany(mappedBy = "theme", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<PartyBooking> partyBookingList;
}

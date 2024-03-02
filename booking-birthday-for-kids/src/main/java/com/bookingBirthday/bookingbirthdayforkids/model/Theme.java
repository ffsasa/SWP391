package com.bookingBirthday.bookingbirthdayforkids.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
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
    private String themeImgUrl;

    @ManyToMany(mappedBy = "themeSet")
    @JsonIgnore
    Set<Venue> venueSet;

    @OneToMany(mappedBy = "theme")
    @JsonManagedReference
    @JsonIgnore
    private List<PartyBooking> partyBookingList;
}

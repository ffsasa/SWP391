package com.bookingBirthday.bookingbirthdayforkids.model;

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
    Set<Venue> venueSet;

    @OneToMany(mappedBy = "theme")
    private List<PartyBooking> partyBookingList;
}

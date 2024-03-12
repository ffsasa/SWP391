package com.bookingBirthday.bookingbirthdayforkids.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Package extends BaseEntity{
    @NotBlank(message = "Package name cannot blank")
    private String packageName;
    @Column(name = "package_description",columnDefinition = "TEXT")
    @NotBlank(message = "Description cannot be blank")
    private String packageDescription;
    @Column(name = "package_img_url",columnDefinition = "TEXT")
    private String packageImgUrl;
    @NotNull(message = "Pricing value cannot be null")
    @Min(value = 0, message = "Pricing value must be greater than or equal to 0")
    private float pricing;

    @OneToMany(mappedBy = "apackage", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<PackageService> packageServiceList;

    @OneToMany(mappedBy = "apackage", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<PartyBooking> partyBookingList;

    @ManyToMany(mappedBy = "packageSet")
    @JsonIgnore
    Set<Venue> venueSet;
}

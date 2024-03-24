package com.bookingBirthday.bookingbirthdayforkids.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;


import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PartyBooking extends BaseEntity{
    @NotBlank(message = "Kid Name cannot blank")
    private String kidName;
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate kidDOB;
    @NotBlank(message = "Email cannot blank")
    @Email(message = "Invalid email format")
    private String email;
    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(regexp = "\\d{10}", message = "Invalid phone number format")
    private String phone;
    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    @ManyToOne
    @JoinColumn(name = "themeInVenue_id")
    private ThemeInVenue themeInVenue;

    @Transient
    @JsonProperty("slotInVenueObject")
    private SlotInVenue slotInVenueObject;

    @Transient
    @JsonProperty("venue")
    private Venue venue;

    @ManyToOne
    @JoinColumn(name = "packageInVenue_id")
    private PackageInVenue packageInVenue;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToMany(mappedBy = "partyBooking", cascade = CascadeType.ALL)
    private List<UpgradeService> upgradeServices;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "partyDated_id", referencedColumnName = "id")
//    @JsonIgnore
    private PartyDated partyDated;

    @OneToMany(mappedBy = "partyBooking", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Payment> paymentList;

    @OneToOne(mappedBy = "partyBooking", cascade = CascadeType.ALL)
    private Review review;

    @Transient
    @JsonProperty("pricingTotal")
    private float pricingTotal;
}

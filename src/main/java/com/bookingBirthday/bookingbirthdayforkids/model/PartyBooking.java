package com.bookingBirthday.bookingbirthdayforkids.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @JsonBackReference
    private ThemeInVenue themeInVenue;


    @ManyToOne
    @JoinColumn(name = "packageInVenue_id")
    @JsonBackReference
    private PackageInVenue packageInVenue;

    @ManyToOne
    @JoinColumn(name = "account_id")
    @JsonBackReference
    private Account account;

    @OneToMany(mappedBy = "partyBooking", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<UpgradeService> upgradeServices;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "partyDated_id", referencedColumnName = "id")
    private PartyDated partyDated;

    @OneToMany(mappedBy = "partyBooking", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Payment> paymentList;

    @OneToMany(mappedBy = "partyBooking", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Review> reviewList;

    @Transient
    @JsonProperty("accountObject")
    private Account accountObject;

    @Transient
    @JsonProperty("themeInVenueObject")
    private ThemeInVenue themeInVenueObject;

    @Transient
    @JsonProperty("packageInVenueObject")
    private PackageInVenue packageInVenueObject;
}

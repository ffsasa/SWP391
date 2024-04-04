package com.bookingBirthday.bookingbirthdayforkids.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;


import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class PartyBooking extends BaseEntity{
    @NotBlank(message = "Kid Name cannot blank")
    private String kidName;
    @NotBlank(message = "Reservation Name cannot blank")
    private String reservationAgent;
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate kidDOB;
    @NotBlank(message = "Email cannot blank")
    @Email(message = "Invalid email format")
    private String email;
    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(regexp = "\\d{10}", message = "Invalid phone number format")
    private String phone;
    @Enumerated(EnumType.STRING)
    public StatusEnum status;
    @Min(value = 1, message = "Capacity value must be greater than or equal to 1")
    private int participantAmount;
    @Min(value = 0, message = "Total price value must be greater than or equal to 0")
    private float totalPrice;
    @Min(value = 0, message = "Deposit value must be greater than or equal to 0")
    private float deposit;
    @Min(value = 0, message = "Remaining money value must be greater than or equal to 0")
    private float remainingMoney;
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToMany(mappedBy = "partyBooking", cascade = CascadeType.ALL)
    private List<UpgradeService> upgradeServices;

    @OneToMany(mappedBy = "partyBooking", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Payment> paymentList;

    @OneToMany(mappedBy = "partyBooking", cascade = CascadeType.ALL)
    private List<PackageInBooking> packageInBookings;

    @OneToOne(mappedBy = "partyBooking", cascade = CascadeType.ALL)
    private Review review;

    @ManyToOne
    @JoinColumn(name = "slotInRoom_id")
    private SlotInRoom slotInRoom;

    @Transient
    @JsonProperty("venueObject")
    private Venue venueObject;
}

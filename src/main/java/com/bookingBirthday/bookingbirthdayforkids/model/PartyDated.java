package com.bookingBirthday.bookingbirthdayforkids.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class PartyDated extends BaseEntity{

    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate date;

    @Transient
    @JsonProperty("slotObject")
    private Slot slotObject;

    @OneToOne(mappedBy = "partyDated", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private PartyBooking partyBooking;

    @ManyToOne
    @JoinColumn(name = "slotInVenue_id")
    @JsonBackReference
    private SlotInVenue slotInVenue;
}

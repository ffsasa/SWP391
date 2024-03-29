package com.bookingBirthday.bookingbirthdayforkids.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @OneToOne(mappedBy = "partyDated", cascade = CascadeType.ALL)
    @JsonIgnore
    private PartyBooking partyBooking;

    @ManyToOne
    @JoinColumn(name = "slotInRoom_id")
    @JsonIgnore
    private SlotInRoom slotInRoom;

}

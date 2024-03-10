package com.bookingBirthday.bookingbirthdayforkids.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class PartyDated extends BaseEntity{
    @JsonIgnore
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDateTime Date;

    @OneToOne(mappedBy = "partyDated", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private PartyBooking partyBooking;

    @ManyToOne
    @JoinColumn(name = "slotInVenue_id")
    @JsonBackReference
    private SlotInVenue slotInVenue;
}

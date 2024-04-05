package com.bookingBirthday.bookingbirthdayforkids.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class SlotInRoom extends BaseEntity{

    @Transient
    @JsonProperty("status")
    private boolean status;

    @Transient
    @JsonProperty("partyBookingId")
    private Long partyBookingId;

    @ManyToOne
    @JoinColumn(name = "slot_id")
    private Slot slot;

    @OneToMany(mappedBy = "slotInRoom", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<PartyBooking> partyBookingList;

    @ManyToOne
    @JoinColumn(name = "room_id")
    @JsonIgnore
    private Room room;
}

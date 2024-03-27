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
@Entity
@Builder
public class SlotInRoom extends BaseEntity{

    @Transient
    @JsonProperty("status")
    private boolean status;

    @Transient
    @JsonProperty("partyDated")
    private PartyDated partyDated;

    @ManyToOne
    @JoinColumn(name = "venue_id")
    @JsonIgnore
    private Venue venue;

    @ManyToOne
    @JoinColumn(name = "slot_id")
    private Slot slot;

    @OneToMany(mappedBy = "slotInRoom", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<PartyDated> partyDatedList;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;
}

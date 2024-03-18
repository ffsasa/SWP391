package com.bookingBirthday.bookingbirthdayforkids.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
public class SlotInVenue extends BaseEntity{

    @Transient
    @JsonProperty("status")
    private boolean status;

    @Transient
    @JsonProperty("partyDatedByDate")
    private PartyDated partyDatedByDate;

    @Transient
    @JsonProperty("slotObject")
    private Slot slotObject;

    @ManyToOne
    @JoinColumn(name = "venue_id")
    @JsonBackReference
    private Venue venue;

    @ManyToOne
    @JoinColumn(name = "slot_id")
    @JsonBackReference
    private Slot slot;

    @OneToMany(mappedBy = "slotInVenue", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    @JsonIgnore
    private List<PartyDated> partyDatedList;
}

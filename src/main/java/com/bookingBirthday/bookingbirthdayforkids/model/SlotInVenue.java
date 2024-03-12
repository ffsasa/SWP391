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
    private boolean status;
    @JsonProperty("status")
    public void setStatus(boolean status) {
        this.status = status;
    }

    @Transient
    private Slot slotObject;
    @JsonProperty("slot")
    public void setSlot(Slot slot) {
        this.slotObject = slot;
    }

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

package com.bookingBirthday.bookingbirthdayforkids.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Room extends BaseEntity{
    private String roomName;
    @Column(name = "room_img_url",columnDefinition = "TEXT")
    private String roomImgUrl;
    private int capacity;
    @ManyToOne
    @JoinColumn(name = "venue_id")
    private Venue venue;

    @OneToMany(mappedBy = "room")
    @JsonIgnore
    private List<SlotInRoom> slotInRoomList;
}

package com.bookingBirthday.bookingbirthdayforkids.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Room extends BaseEntity{
    @NotBlank(message = "Room Name cannot blank")
    private String roomName;
    @Column(name = "room_img_url",columnDefinition = "TEXT")
    private String roomImgUrl;
    @Min(value = 20, message = "Capacity value must be greater than or equal to 20")
    private int capacity;
    @Min(value = 0, message = "Pricing value must be greater than or equal to 0")
    private float pricing;

    @ManyToOne
    @JoinColumn(name = "venue_id")
    @JsonIgnore
    private Venue venue;

    @Transient
    @JsonProperty("venueInfo")
    private Venue venueInfo;

    @OneToMany(mappedBy = "room")
//    @JsonIgnore
    private List<SlotInRoom> slotInRoomList;
}

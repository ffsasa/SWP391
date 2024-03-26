package com.bookingBirthday.bookingbirthdayforkids.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}

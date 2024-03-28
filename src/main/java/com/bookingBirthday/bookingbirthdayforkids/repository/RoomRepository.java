package com.bookingBirthday.bookingbirthdayforkids.repository;

import com.bookingBirthday.bookingbirthdayforkids.model.Room;
import com.bookingBirthday.bookingbirthdayforkids.model.Slot;
import com.bookingBirthday.bookingbirthdayforkids.model.Venue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findAllByIsActiveIsTrue();
    boolean existsByRoomNameAndVenue(String roomName, Venue venue);
}

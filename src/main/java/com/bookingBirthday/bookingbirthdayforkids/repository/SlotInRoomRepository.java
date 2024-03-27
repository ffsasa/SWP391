package com.bookingBirthday.bookingbirthdayforkids.repository;

import com.bookingBirthday.bookingbirthdayforkids.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlotInRoomRepository extends JpaRepository<SlotInRoom, Long> {
    boolean existsBySlotIdAndVenueId(Long slot_id, Long venue_id);
    SlotInRoom findByVenueAndSlot(Venue venue, Slot slot);
}

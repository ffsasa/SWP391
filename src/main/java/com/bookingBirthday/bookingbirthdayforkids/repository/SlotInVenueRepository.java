package com.bookingBirthday.bookingbirthdayforkids.repository;

import com.bookingBirthday.bookingbirthdayforkids.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlotInVenueRepository extends JpaRepository<SlotInVenue, Long> {
    boolean existsBySlotIdAndVenueId(Long slot_id, Long venue_id);
    SlotInVenue findByVenueAndSlot(Venue venue, Slot slot);
}

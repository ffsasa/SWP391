package com.bookingBirthday.bookingbirthdayforkids.repository;

import com.bookingBirthday.bookingbirthdayforkids.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SlotInRoomRepository extends JpaRepository<SlotInRoom, Long> {
    boolean existsBySlotIdAndRoomId(Long slot_id, Long room_id);
    SlotInRoom findByRoomAndSlot(Room room, Slot slot);
}

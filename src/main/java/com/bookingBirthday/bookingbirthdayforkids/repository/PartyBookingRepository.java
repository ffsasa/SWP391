package com.bookingBirthday.bookingbirthdayforkids.repository;

import com.bookingBirthday.bookingbirthdayforkids.model.PartyBooking;
import com.bookingBirthday.bookingbirthdayforkids.model.StatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PartyBookingRepository extends JpaRepository<PartyBooking, Long> {
    List<PartyBooking> findAllByIsActiveIsTrue();
    List<PartyBooking> findAllByIsActiveIsTrueAndAccountId(Long id);

    List<PartyBooking> findAllByStatus(StatusEnum statusEnum );

    List<PartyBooking> findAllByDateAndIsActiveIsTrue(LocalDate date);

    Optional<PartyBooking> findByIdAndIsActiveIsTrue(Long id);

    Boolean existsBySlotInRoomIdAndDateAndIsActiveIsTrue(Long slotInRoomId, LocalDate date);
}

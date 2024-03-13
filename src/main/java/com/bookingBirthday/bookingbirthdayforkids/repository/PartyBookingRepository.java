package com.bookingBirthday.bookingbirthdayforkids.repository;

import com.bookingBirthday.bookingbirthdayforkids.model.PartyBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartyBookingRepository extends JpaRepository<PartyBooking, Long> {
    List<PartyBooking> findAllByIsActiveIsTrue();
    List<PartyBooking> findAllByIsActiveIsTrueAndAccountId(Long id);
}

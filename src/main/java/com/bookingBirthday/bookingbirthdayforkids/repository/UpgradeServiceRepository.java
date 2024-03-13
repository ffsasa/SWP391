package com.bookingBirthday.bookingbirthdayforkids.repository;

import com.bookingBirthday.bookingbirthdayforkids.model.PartyBooking;
import com.bookingBirthday.bookingbirthdayforkids.model.Services;
import com.bookingBirthday.bookingbirthdayforkids.model.UpgradeService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UpgradeServiceRepository extends JpaRepository<UpgradeService, Long> {
    Optional<UpgradeService> findByPartyBookingAndServices(PartyBooking partyBooking, Services services);
    List<UpgradeService> findAllByIsActiveIsTrue();
}

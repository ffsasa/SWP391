package com.bookingBirthday.bookingbirthdayforkids.repository;

import com.bookingBirthday.bookingbirthdayforkids.model.PartyDated;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PartyDatedRepository extends JpaRepository<PartyDated, Long> {

    List<PartyDated> findAllByIsActiveIsTrue();
    Optional<PartyDated> findPartyDatedByPartyBookingId(Long id);
    List<PartyDated> findByDate(LocalDate date);
}

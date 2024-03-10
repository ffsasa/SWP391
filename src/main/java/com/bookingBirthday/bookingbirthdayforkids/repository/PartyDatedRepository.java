package com.bookingBirthday.bookingbirthdayforkids.repository;

import com.bookingBirthday.bookingbirthdayforkids.model.Account;
import com.bookingBirthday.bookingbirthdayforkids.model.Package;
import com.bookingBirthday.bookingbirthdayforkids.model.PartyDated;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PartyDatedRepository extends JpaRepository<PartyDated, Long> {

    List<PartyDated> findAllByIsActiveIsTrue();
    Optional<PartyDated> findPartyDatedByPartyBookingId(Long id);
}

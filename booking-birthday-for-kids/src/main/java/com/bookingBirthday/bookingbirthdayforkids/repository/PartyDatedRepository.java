package com.bookingBirthday.bookingbirthdayforkids.repository;

import com.bookingBirthday.bookingbirthdayforkids.model.Account;
import com.bookingBirthday.bookingbirthdayforkids.model.PartyDated;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartyDatedRepository extends JpaRepository<PartyDated, Long> {
    boolean existsById(Long id);

}
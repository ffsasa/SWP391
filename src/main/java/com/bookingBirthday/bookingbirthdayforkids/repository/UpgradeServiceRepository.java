package com.bookingBirthday.bookingbirthdayforkids.repository;

import com.bookingBirthday.bookingbirthdayforkids.model.UpgradeService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UpgradeServiceRepository extends JpaRepository<UpgradeService, Long> {
    List<UpgradeService> findAllByIsActiveIsTrue();
}

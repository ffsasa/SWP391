package com.bookingBirthday.bookingbirthdayforkids.repository;

import com.bookingBirthday.bookingbirthdayforkids.model.PackageService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PackageServiceRepository extends JpaRepository<PackageService, Long> {
    List<PackageService> findAllByIsActiveIsTrue();
}

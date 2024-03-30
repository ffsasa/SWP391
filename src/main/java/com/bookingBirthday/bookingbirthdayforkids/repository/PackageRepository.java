package com.bookingBirthday.bookingbirthdayforkids.repository;

import com.bookingBirthday.bookingbirthdayforkids.model.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PackageRepository extends JpaRepository<Package, Long> {

    boolean existsByPackageName(String packageName);
    List<Package> findAllByVenueId(Long venueId);
    List<Package> findAllByIsActiveIsTrue();

}

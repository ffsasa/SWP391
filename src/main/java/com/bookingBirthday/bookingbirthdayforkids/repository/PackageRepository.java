package com.bookingBirthday.bookingbirthdayforkids.repository;

import com.bookingBirthday.bookingbirthdayforkids.model.Package;
import com.bookingBirthday.bookingbirthdayforkids.model.TypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PackageRepository extends JpaRepository<Package, Long> {
    List<Package> findAllByVenueId(Long venueId);
    List<Package> findAllByVenueIdAndPackageTypeAndIsActiveIsTrue(Long venueId, TypeEnum typeEnum);
    List<Package> findAllByVenueIdAndIsActiveIsTrue(Long venueId);
    List<Package> findAllByVenueIdAndIsActiveIsFalse(Long venueId);
    Optional<Package> findByVenueIdAndId(Long venueId, Long packageId);
    List<Package> findAllByIsActiveIsTrue();
    List<Package> findAllByVenueIdAndPackageType(Long venueId, TypeEnum typeEnum);
    List<Package> findActivePackagesByVenueIdAndPackageType(Long venueId, TypeEnum typeEnum);
    List<Package> findInactivePackagesByVenueIdAndPackageType(Long venueId, TypeEnum typeEnum);
}

package com.bookingBirthday.bookingbirthdayforkids.repository;

import com.bookingBirthday.bookingbirthdayforkids.model.*;
import com.bookingBirthday.bookingbirthdayforkids.model.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PackageInVenueRepository extends JpaRepository<PackageInVenue, Long> {
    List<PackageInVenue> findAllByIsActiveIsTrue();
    PackageInVenue findByVenueAndApackage(Venue venue, Package aPackage);
}

package com.bookingBirthday.bookingbirthdayforkids.repository;

import com.bookingBirthday.bookingbirthdayforkids.controller.ThemeInVenueController;
import com.bookingBirthday.bookingbirthdayforkids.model.PackageInVenue;
import com.bookingBirthday.bookingbirthdayforkids.model.Theme;
import com.bookingBirthday.bookingbirthdayforkids.model.ThemeInVenue;
import com.bookingBirthday.bookingbirthdayforkids.model.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThemeInVenueRepository extends JpaRepository<ThemeInVenue, Long> {
    List<ThemeInVenue> findAllByIsActiveIsTrue();
    ThemeInVenue findByVenueAndTheme(Venue venue, Theme theme);
}

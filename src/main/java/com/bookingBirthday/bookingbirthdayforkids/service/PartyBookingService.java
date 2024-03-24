package com.bookingBirthday.bookingbirthdayforkids.service;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.PartyBookingRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.model.PartyBooking;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface PartyBookingService {
    public ResponseEntity<ResponseObj> getAll();

    public ResponseEntity<ResponseObj> getAllForHost();

    public void updateCronJob(Long bookingId, PartyBooking partyBooking);

    public ResponseEntity<ResponseObj> getById(Long id);

    public ResponseEntity<ResponseObj> getById_ForCustomer(Long id);

    public ResponseEntity<ResponseObj> create(PartyBookingRequest partyBookingRequest);

    public ResponseEntity<ResponseObj> update(Long id, PartyBookingRequest partyBookingRequest);

    public ResponseEntity<ResponseObj> delete(Long id);

    public ResponseEntity<ResponseObj> getAllByUser();

    public ResponseEntity<ResponseObj> Cancel(Long bookingId);

    @Transactional
    ResponseEntity<ResponseObj> cancelBookingForHost(Long bookingId);

    @Transactional
    ResponseEntity<ResponseObj> completeBookingForHost(Long bookingId);

    @Transactional
    ResponseEntity<ResponseObj> cancelBookingForCustomer(Long bookingId);

    public List<PartyBooking> findConfirmedBookings();

    public ResponseEntity<ResponseObj> updateThemeInVenue(Long partyBookingId, Long themeInVenueId);

    public ResponseEntity<ResponseObj> updatePackageInVenue(Long partyBookingId, Long packageInVenueId);
    public ResponseEntity<ResponseObj> getAllCompleted();

}

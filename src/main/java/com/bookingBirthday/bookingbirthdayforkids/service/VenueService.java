package com.bookingBirthday.bookingbirthdayforkids.service;

import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface VenueService {
    public ResponseEntity<ResponseObj> getAll();

    public ResponseEntity<ResponseObj> getAllForHost();

//    public ResponseEntity<ResponseObj> checkSlotInVenue(LocalDateTime date);
//
    public ResponseEntity<ResponseObj> getById(Long id);

    public ResponseEntity<ResponseObj> getById_ForCustomer(Long id);

    public ResponseEntity<ResponseObj> activeVenue(Long id);

    public ResponseEntity<ResponseObj> create(MultipartFile imgFile, String venueName, String venueDescription, String street, String ward, String district, String city);

//    public ResponseEntity<ResponseObj> update(Long id, MultipartFile imgFile, String venueName, String venueDescription, String street, String ward, String district, String city);

    public ResponseEntity<ResponseObj> delete(Long id);
    public ResponseEntity<ResponseObj> addPackage(Long venueId, Long packageId);

    public ResponseEntity<ResponseObj> getPackageInVenueByVenue(Long venueId);

    public ResponseEntity<ResponseObj> getAllPartyBookingByVenue(Long venueId);
//
//
//    public ResponseEntity<ResponseObj> getSlotInVenueById(Long venueId);
//
//    public ResponseEntity<ResponseObj> getAllSlotHaveNotAddByVenue(Long venueId);
//

//
    public ResponseEntity<ResponseObj> getAllPackageHaveNotAddByVenune(Long venueId);
//
//    public ResponseEntity<ResponseObj> checkSlotInVenueForHost(LocalDate date);
}

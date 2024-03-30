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

    public ResponseEntity<ResponseObj> getById_ForCustomer(Long id);
    public ResponseEntity<ResponseObj> getAllPartyBookingByVenue(Long venueId);
    public ResponseEntity<ResponseObj> activeVenue();

    public ResponseEntity<ResponseObj> customize(MultipartFile imgFile, String venueName, String venueDescription, String street, String ward, String district, String city);

    public ResponseEntity<ResponseObj> update(MultipartFile imgFile, String venueName, String venueDescription, String street, String ward, String district, String city);

    public ResponseEntity<ResponseObj> delete();
//    public ResponseEntity<ResponseObj> getAllPartyBookingByVenue(Long venueId);

}

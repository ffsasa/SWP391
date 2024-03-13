package com.bookingBirthday.bookingbirthdayforkids.service;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.VenueRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public interface VenueService {
    public ResponseEntity<ResponseObj> getAll();

    public ResponseEntity<ResponseObj> checkSlotInVenue(LocalDate date);

    public ResponseEntity<ResponseObj> getById(Long id);

    public ResponseEntity<ResponseObj> create(MultipartFile imgFile, String venueName, String venueDescription, String location, int capacity);

    public ResponseEntity<ResponseObj> update(Long id, MultipartFile imgFile, String venueName, String venueDescription, String location, int capacity);

    public ResponseEntity<ResponseObj> delete(Long id);

    public ResponseEntity<ResponseObj> addTheme(Long venueId, Long themeId);

    public ResponseEntity<ResponseObj> addPackage(Long venueId, Long packageId);
}
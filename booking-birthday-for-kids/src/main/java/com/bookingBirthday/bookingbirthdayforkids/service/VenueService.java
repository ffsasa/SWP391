package com.bookingBirthday.bookingbirthdayforkids.service;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.VenueRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import org.springframework.http.ResponseEntity;

public interface VenueService {
    public ResponseEntity<ResponseObj> getAll();

    public ResponseEntity<ResponseObj> getById(Long id);

    public ResponseEntity<ResponseObj> create(VenueRequest venueRequest);

    public ResponseEntity<ResponseObj> update(Long id, VenueRequest venueRequest);

    public ResponseEntity<ResponseObj> delete(Long id);
}

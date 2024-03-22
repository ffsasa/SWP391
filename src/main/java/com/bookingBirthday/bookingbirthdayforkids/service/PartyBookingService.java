package com.bookingBirthday.bookingbirthdayforkids.service;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.PartyBookingRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import org.springframework.http.ResponseEntity;

public interface PartyBookingService {
    public ResponseEntity<ResponseObj> getAll();

    public ResponseEntity<ResponseObj> getAllForHost();

    public ResponseEntity<ResponseObj> getById(Long id);

    public ResponseEntity<ResponseObj> getById_ForCustomer(Long id);

    public ResponseEntity<ResponseObj> create(PartyBookingRequest partyBookingRequest);

    public ResponseEntity<ResponseObj> update(Long id, PartyBookingRequest partyBookingRequest);

    public ResponseEntity<ResponseObj> delete(Long id);
    public ResponseEntity<ResponseObj> getAllByUser();
}

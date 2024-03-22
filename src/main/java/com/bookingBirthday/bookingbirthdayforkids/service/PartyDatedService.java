package com.bookingBirthday.bookingbirthdayforkids.service;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.PartyDatedRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import org.springframework.http.ResponseEntity;

public interface PartyDatedService {
    public ResponseEntity<ResponseObj> getAll();

    public ResponseEntity<ResponseObj> getAllForHost();

    public ResponseEntity<ResponseObj> getById(Long id);
    public ResponseEntity<ResponseObj> getById_ForCustomer(Long id);

    public ResponseEntity<ResponseObj> update(Long id, PartyDatedRequest partyDatedRequest);

    public ResponseEntity<ResponseObj> delete(Long id);

    public ResponseEntity<ResponseObj> getPartyBookingByPartyDateId(Long id);
}

package com.bookingBirthday.bookingbirthdayforkids.service;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.PartyDatedRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import org.springframework.http.ResponseEntity;

public interface PartyDatedService {
    public ResponseEntity<ResponseObj> getAll();

    public ResponseEntity<ResponseObj> getById(Long id);

    public ResponseEntity<ResponseObj> create(PartyDatedRequest partyDatedRequest);

    public ResponseEntity<ResponseObj> update(Long id, PartyDatedRequest partyDatedRequest);

    public ResponseEntity<ResponseObj> delete(Long id);
}

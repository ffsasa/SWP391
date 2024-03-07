package com.bookingBirthday.bookingbirthdayforkids.service;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.UpgradeServiceRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import org.springframework.http.ResponseEntity;

public interface PartyBookingService {
    public ResponseEntity<ResponseObj> getAll();

    public ResponseEntity<ResponseObj> getById(Long id);

    public ResponseEntity<ResponseObj> create(UpgradeServiceRequest upgradeServiceRequest);

    public ResponseEntity<ResponseObj> update(Long id, UpgradeServiceRequest upgradeServiceRequest);

    public ResponseEntity<ResponseObj> delete(Long id);
}

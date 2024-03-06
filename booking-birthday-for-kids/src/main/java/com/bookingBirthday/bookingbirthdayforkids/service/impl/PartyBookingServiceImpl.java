package com.bookingBirthday.bookingbirthdayforkids.service.impl;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.UpgradeServiceRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.service.PartyBookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PartyBookingServiceImpl implements PartyBookingService {
    @Override
    public ResponseEntity<ResponseObj> getAll() {
        return null;
    }

    @Override
    public ResponseEntity<ResponseObj> getById(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseObj> create(UpgradeServiceRequest upgradeServiceRequest) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseObj> update(Long id, UpgradeServiceRequest upgradeServiceRequest) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseObj> delete(Long id) {
        return null;
    }
}

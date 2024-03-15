package com.bookingBirthday.bookingbirthdayforkids.service;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.PackageInVenueRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.request.SlotInVenueRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import org.springframework.http.ResponseEntity;

public interface PackageInVenueService {
    public ResponseEntity<ResponseObj> getAll();

    public ResponseEntity<ResponseObj> getById(Long id);

    ResponseEntity<ResponseObj> update(Long id, PackageInVenueRequest packageInVenueRequest);


    ResponseEntity<ResponseObj> delete(Long id);

}
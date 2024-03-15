package com.bookingBirthday.bookingbirthdayforkids.service;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.PackageInVenueRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.request.ThemeInVenueRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import org.springframework.http.ResponseEntity;

public interface ThemeInVenueService {
    public ResponseEntity<ResponseObj> getAll();

    public ResponseEntity<ResponseObj> getById(Long id);

    ResponseEntity<ResponseObj> update(Long id, ThemeInVenueRequest themeInVenueRequest);


    ResponseEntity<ResponseObj> delete(Long id);
}
package com.bookingBirthday.bookingbirthdayforkids.service;

import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.dto.request.PackageRequest;
import org.springframework.http.ResponseEntity;

public interface PackageService {
    public ResponseEntity<ResponseObj> getAll();

    public ResponseEntity<ResponseObj> getById(Long id);

    public ResponseEntity<ResponseObj> create(PackageRequest packageRequest);

    public ResponseEntity<ResponseObj> update(Long id, PackageRequest packageRequest);

    public ResponseEntity<ResponseObj> delete(Long id);
}

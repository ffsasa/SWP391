package com.bookingBirthday.bookingbirthdayforkids.service;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.PackageServiceRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import org.springframework.http.ResponseEntity;

public interface PackageServiceService {
    public ResponseEntity<ResponseObj> getAll();

    public ResponseEntity<ResponseObj> getById(Long id);

    public ResponseEntity<ResponseObj> create(PackageServiceRequest packageServiceRequest);

    public ResponseEntity<ResponseObj> update(Long id, PackageServiceRequest packageServiceRequestRequest);

    public ResponseEntity<ResponseObj> delete(Long id);
}
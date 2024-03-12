package com.bookingBirthday.bookingbirthdayforkids.service;

import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.dto.request.PackageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface PackageService {
    public ResponseEntity<ResponseObj> getAll();

    public ResponseEntity<ResponseObj> getById(Long id);

    public ResponseEntity<ResponseObj> create(MultipartFile imgFile, String packageName, String packageDescription, float pricing);
    public ResponseEntity<ResponseObj> update(Long id, MultipartFile imgFile, String packageName, String packageDescription, float pricing);

    public ResponseEntity<ResponseObj> delete(Long id);
}

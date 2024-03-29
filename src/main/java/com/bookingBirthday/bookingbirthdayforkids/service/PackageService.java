package com.bookingBirthday.bookingbirthdayforkids.service;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.PackageServiceRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.model.TypeEnum;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PackageService {
    public ResponseEntity<ResponseObj> getAllForCustomer(Long venueId);

    public ResponseEntity<ResponseObj> getAllForHost(Long venueId);

    public ResponseEntity<ResponseObj> getByIdForHost(Long venueId, Long id);

    public ResponseEntity<ResponseObj> getByIdForCustomer(Long venueId, Long id);

    ResponseEntity<ResponseObj> create(Long venueId, MultipartFile imgFile, String packageName, String packageDescription, float percent, List<PackageServiceRequest> packageServiceRequestList, TypeEnum typeEnum);

    public ResponseEntity<ResponseObj> update(Long venueId, Long id, MultipartFile imgFile, String packageName, String packageDescription);

    public ResponseEntity<ResponseObj> updatePercentPackage(Long venueId, Long id, float percent);

    public ResponseEntity<ResponseObj> delete(Long venueId, Long id);
}

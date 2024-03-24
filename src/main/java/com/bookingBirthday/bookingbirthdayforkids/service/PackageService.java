package com.bookingBirthday.bookingbirthdayforkids.service;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.PackageServiceRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PackageService {
    public ResponseEntity<ResponseObj> getAll();

    public ResponseEntity<ResponseObj> getAllForHost();

    public ResponseEntity<ResponseObj> getById(Long id);

    public ResponseEntity<ResponseObj> getById_ForCustomer(Long id);

    public ResponseEntity<ResponseObj> create(MultipartFile imgFile, String packageName, String packageDescription, float percent,List<PackageServiceRequest> packageServiceRequestList);
    public ResponseEntity<ResponseObj> update(Long id, MultipartFile imgFile, String packageName, String packageDescription);
    public ResponseEntity<ResponseObj> updatePercentPackage(Long id, float percent);

    public ResponseEntity<ResponseObj> delete(Long id);
    public ResponseEntity<ResponseObj> addPackageInVenueByVenueId(Long venueId, List<Long> packageIdList);

}

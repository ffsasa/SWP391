package com.bookingBirthday.bookingbirthdayforkids.service;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.PackageServiceRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.model.TypeEnum;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PackageService {
    public ResponseEntity<ResponseObj> getAllForCustomer(Long id);

    public ResponseEntity<ResponseObj> getAllForHost();

    public ResponseEntity<ResponseObj> getByIdForHost(Long id);

    public ResponseEntity<ResponseObj> getByIdForCustomer(Long id);

    ResponseEntity<ResponseObj> create(MultipartFile imgFile, String packageName, String packageDescription, float percent, List<PackageServiceRequest> packageServiceRequestList, TypeEnum typeEnum);

    public ResponseEntity<ResponseObj> update(Long venueId, Long id, MultipartFile imgFile, String packageName, String packageDescription);

    public ResponseEntity<ResponseObj> updatePercentPackage(Long venueId, Long id, float percent);

    public ResponseEntity<ResponseObj> delete(Long id);

    ResponseEntity<ResponseObj> enablePackageForHost(Long id);

    public ResponseEntity<ResponseObj> getAllForCustomerByType(Long venueId, TypeEnum typeEnum);
}

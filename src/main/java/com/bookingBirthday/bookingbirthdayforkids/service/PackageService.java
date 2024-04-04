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

    public ResponseEntity<ResponseObj> getAllForHostIsTrue();

    public ResponseEntity<ResponseObj> getAllForHostIsFalse();

    public ResponseEntity<ResponseObj> getByIdForCustomer(Long id);

    ResponseEntity<ResponseObj> create(MultipartFile imgFile, String packageName, String packageDescription, float percent, List<PackageServiceRequest> packageServiceRequestList, TypeEnum typeEnum);

    public ResponseEntity<ResponseObj> update( Long id, MultipartFile imgFile, String packageName, String packageDescription);

    public ResponseEntity<ResponseObj> updatePercentPackage(Long id, float percent);

    public ResponseEntity<ResponseObj> delete(Long id);

    ResponseEntity<ResponseObj> enablePackageForHost(Long id);

    public ResponseEntity<ResponseObj> getAllForCustomerByType(Long venueId, TypeEnum typeEnum);

    ResponseEntity<ResponseObj> getAllForHostByType(TypeEnum typeEnum);

    ResponseEntity<ResponseObj> getAllForHostIsTrueByType(TypeEnum typeEnum);

    ResponseEntity<ResponseObj> getAllForHostIsFalseByType(TypeEnum typeEnum);

    ResponseEntity<ResponseObj> getAllPackageByPartyBookingId(Long partyBookingId);

    ResponseEntity<ResponseObj> getAllPackageByPartyBookingIdAndType(Long partyBookingId, TypeEnum typeEnum);
}

package com.bookingBirthday.bookingbirthdayforkids.service;

import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.model.TypeEnum;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ServicesService {
    public ResponseEntity<ResponseObj> getAllServiceByVenue(Long venueId);

    public ResponseEntity<ResponseObj> getAllServiceByTypeByVenue(TypeEnum typeEnum, Long venueId);

    public ResponseEntity<ResponseObj> getAllServiceTypeByHost(TypeEnum typeEnum);
    public ResponseEntity<ResponseObj> getAllForHost();

    public ResponseEntity<ResponseObj> getAllServiceTypeIsTrueByHost(TypeEnum typeEnum);

    public ResponseEntity<ResponseObj> getAllServiceTypeIsFalseByHost(TypeEnum typeEnum);

    public ResponseEntity<ResponseObj> getAllServiceIsActiveTrueForHost();

    public ResponseEntity<ResponseObj> getAllServiceIsActiveFalseForHost();

    public ResponseEntity<ResponseObj> getServiceByIdForCustomerByVenue(Long venueId, Long serviceId);

    public ResponseEntity<ResponseObj> enable(Long serviceId);

    public ResponseEntity<ResponseObj> getByIdByHost(Long id);

    public ResponseEntity<ResponseObj> create(MultipartFile imgFile, String serviceName, String description, float pricing, TypeEnum typeEnum);

    public ResponseEntity<ResponseObj> update(Long id, MultipartFile imgFile, String serviceName, String description, float pricing,TypeEnum typeEnum);

    public ResponseEntity<ResponseObj> delete(Long id);
}

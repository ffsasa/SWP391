package com.bookingBirthday.bookingbirthdayforkids.service;

import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.model.TypeEnum;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ServicesService {
    public ResponseEntity<ResponseObj> getAll();

    public ResponseEntity<ResponseObj> getAllServiceByType(TypeEnum typeEnum);
    public ResponseEntity<ResponseObj> getAllForHost();

    public  ResponseEntity<ResponseObj> getById_ForCustomer(Long id);


    public ResponseEntity<ResponseObj> getById(Long id);

    public ResponseEntity<ResponseObj> create(MultipartFile imgFile, String serviceName, String description, float pricing, TypeEnum typeEnum);

    public ResponseEntity<ResponseObj> update(Long id, MultipartFile imgFile, String serviceName, String description, float pricing,TypeEnum typeEnum);

    public ResponseEntity<ResponseObj> delete(Long id);
}

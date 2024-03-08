package com.bookingBirthday.bookingbirthdayforkids.service;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.AccountRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.request.ServicesRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import org.springframework.http.ResponseEntity;

public interface ServicesService {
    public ResponseEntity<ResponseObj> getAll();

    public ResponseEntity<ResponseObj> getById(Long id);

    public ResponseEntity<ResponseObj> create(ServicesRequest servicesRequest);

    public ResponseEntity<ResponseObj> update(Long id, ServicesRequest servicesRequest);

    public ResponseEntity<ResponseObj> delete(Long id);
}

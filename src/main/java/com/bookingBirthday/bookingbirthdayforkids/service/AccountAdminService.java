package com.bookingBirthday.bookingbirthdayforkids.service;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.AccountRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import org.springframework.http.ResponseEntity;

public interface AccountAdminService {
    public ResponseEntity<ResponseObj> getAllCustomer();
    public ResponseEntity<ResponseObj> getAllHost();

    public ResponseEntity<ResponseObj> getById(Long id);

    public ResponseEntity<?> create(AccountRequest accountRequest);
    public ResponseEntity<ResponseObj> information();
    public ResponseEntity<ResponseObj> delete(Long id);
}

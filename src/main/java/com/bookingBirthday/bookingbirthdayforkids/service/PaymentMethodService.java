package com.bookingBirthday.bookingbirthdayforkids.service;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.PaymentMethodRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import org.springframework.http.ResponseEntity;

public interface PaymentMethodService {
    public ResponseEntity<ResponseObj> getAll();

    public ResponseEntity<ResponseObj> getById(Long id);

    public ResponseEntity<ResponseObj> create(PaymentMethodRequest paymentMethodRequest);

    public ResponseEntity<ResponseObj> update(Long id, PaymentMethodRequest paymentMethodRequest);

    public ResponseEntity<ResponseObj> delete(Long id);
}

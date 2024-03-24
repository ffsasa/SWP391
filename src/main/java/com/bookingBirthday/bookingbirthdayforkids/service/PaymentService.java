package com.bookingBirthday.bookingbirthdayforkids.service;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.PaymentRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.io.UnsupportedEncodingException;

public interface PaymentService {

    public  String payWithVNPAYOnline(Long bookingId, HttpServletRequest request) throws UnsupportedEncodingException;
    public ResponseEntity<ResponseObj> getAll();

    public ResponseEntity<ResponseObj> getById(Long id);

    void paymentSuccess(Long id);

//    public ResponseEntity<ResponseObj> create(PaymentRequest paymentRequest);

//    public ResponseEntity<ResponseObj> update(Long id, PaymentRequest paymentRequest);
//
//    public ResponseEntity<ResponseObj> delete(Long id);
}

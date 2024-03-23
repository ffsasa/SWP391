package com.bookingBirthday.bookingbirthdayforkids.controller;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.PaymentRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.repository.PaymentRepository;
import com.bookingBirthday.bookingbirthdayforkids.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
@Slf4j
@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    @Autowired
    PaymentService paymentService;


    @GetMapping("/getAll-payment")
    public ResponseEntity<ResponseObj> getAll() {
        return paymentService.getAll();
    }

    @GetMapping("/getId-payment/{id}")
    public ResponseEntity<ResponseObj> getByid(@PathVariable Long id) {
        return paymentService.getById(id);
    }

    @PostMapping("/payment-vnpay")
    public String pay(@RequestParam Long bookingId, HttpServletRequest request){
        try {
            return paymentService.payWithVNPAYOnline(bookingId, request);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }


    @GetMapping("/payment-callback")
    public ResponseEntity<Boolean> paymentCallback(@RequestParam Map<String, String> queryParams, HttpServletResponse response) throws IOException, IOException {
       String vnp_ResponseCode = queryParams.get("vnp_ResponseCode");
        Long bookingId = Long.parseLong(queryParams.get("vnp_OrderInfo"));
//        Long paymentId = Long.parseLong(queryParams.get("vnp_PaymentInfo"));

        if ("00".equals(vnp_ResponseCode)) {
            paymentService.paymentSuccess(bookingId);

            response.sendRedirect("http://localhost:3000/payment/success/"+bookingId);

            return ResponseEntity.ok(true);
        } else{


            response.sendRedirect("http://localhost:3000/payment/fail");

    }
     return ResponseEntity.ok(false);
   }
}




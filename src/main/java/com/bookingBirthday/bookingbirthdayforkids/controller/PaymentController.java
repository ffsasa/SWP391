package com.bookingBirthday.bookingbirthdayforkids.controller;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.PaymentRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.model.PartyBooking;
import com.bookingBirthday.bookingbirthdayforkids.model.Payment;
import com.bookingBirthday.bookingbirthdayforkids.model.PaymentMethod;
import com.bookingBirthday.bookingbirthdayforkids.model.StatusEnum;
import com.bookingBirthday.bookingbirthdayforkids.repository.PartyBookingRepository;
import com.bookingBirthday.bookingbirthdayforkids.repository.PaymentMethodRepository;
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
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    @Autowired
    PaymentService paymentService;

    @Autowired
    PartyBookingRepository partyBookingRepository;

    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    PaymentMethodRepository paymentMethodRepository;

    @GetMapping("/getAll-payment")
    public ResponseEntity<ResponseObj> getAll() {
        return paymentService.getAll();
    }

    @GetMapping("/getId-payment/{id}")
    public ResponseEntity<ResponseObj> getByid(@PathVariable Long id) {
        return paymentService.getById(id);
    }

    @PostMapping("/payment-vnpay")
    public String pay(@RequestParam Long bookingId, @RequestParam Long paymentMethodId,HttpServletRequest request){
        try {
            return paymentService.payWithVNPAYOnline(bookingId, paymentMethodId, request);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }




    @GetMapping("/payment-callback")
    public ResponseEntity<Boolean> paymentCallback(@RequestParam Map<String, String> queryParams, HttpServletResponse response) throws IOException, IOException {
        String vnp_ResponseCode = queryParams.get("vnp_ResponseCode");
        Long bookingId = Long.parseLong(queryParams.get("vnp_OrderInfo"));
//        Long paymentMethodId = Long.parseLong(queryParams.get("vnp_OrderType"));
        float vnp_Amount = Float.parseFloat(queryParams.get("vnp_Amount"));

        if ("00".equals(vnp_ResponseCode)) {
            Optional<PartyBooking> partyBooking = partyBookingRepository.findById(bookingId);
            PaymentMethod paymentMethod = paymentMethodRepository.findById(1L).get();

            Payment payment = new Payment();
            payment.setPartyBooking(partyBooking.get());
            payment.setCreateAt(LocalDateTime.now());
            payment.setActive(true);
            payment.setAmount(vnp_Amount/100);
            payment.setStatus("SUCCESS");
            payment.setPaymentMethod(paymentMethod);
            paymentRepository.save(payment);
            paymentService.paymentSuccess(bookingId);

            response.sendRedirect("http://localhost:3000/payment/success/"+bookingId);

            return ResponseEntity.ok(true);
        } else{
            Optional<PartyBooking> partyBooking = partyBookingRepository.findById(bookingId);
            PaymentMethod paymentMethod = paymentMethodRepository.findById(1L).get();
            Payment payment = new Payment();
            payment.setPartyBooking(partyBooking.get());
            payment.setCreateAt(LocalDateTime.now());
            payment.setActive(true);
            payment.setAmount(vnp_Amount/100);
            payment.setStatus("FAILED");
            payment.setPaymentMethod(paymentMethod);
            paymentRepository.save(payment);
            paymentService.paymentFail(bookingId);
            response.sendRedirect("http://localhost:3000/payment/failed");

    }
     return ResponseEntity.ok(false);
   }
}




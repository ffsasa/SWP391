package com.bookingBirthday.bookingbirthdayforkids.service.impl;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.PaymentRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.model.*;
import com.bookingBirthday.bookingbirthdayforkids.repository.*;
import com.bookingBirthday.bookingbirthdayforkids.service.PaymentService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    PartyBookingRepository partyBookingRepository;

    @Autowired
    PaymentMethodRepository paymentMethodRepository;

    @Autowired
    AccountRepository accountRepository;

    @Override
    public ResponseEntity<ResponseObj> getAll() {
        try {
            List<Payment> paymentList = paymentRepository.findAll();
            if (paymentList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "List is empty", null));
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", paymentList));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> getById(Long id) {
        try {
            Optional<Payment> payment = paymentRepository.findById(id);
            if(payment.isPresent())
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), null, payment));
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Payment does not exist", null));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> create(PaymentRequest paymentRequest) {
        Payment payment = new Payment();
        Account account = accountRepository.findById(paymentRequest.getAccountID()).get();
        PaymentMethod paymentMethod = paymentMethodRepository.findById(paymentRequest.getPaymentMethodID()).get();
        PartyBooking partyBooking = partyBookingRepository.findById(paymentRequest.getBookingID()).get();
        payment.setAccount(account);
        payment.setPaymentMethod(paymentMethod);
        payment.setPartyBooking(partyBooking);
        payment.setCreateAt(LocalDateTime.now());
        payment.setStatus(StatusEnum.PENDING);
        payment.setActive(true);

        LocalDateTime expireDate =  payment.getCreateAt().plus(30, ChronoUnit.DAYS);
        payment.setExpireDate(expireDate);
        paymentRepository.save(payment);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(),"Create successful", payment));
    }

    @Override
    public ResponseEntity<ResponseObj> update(Long id, PaymentRequest paymentRequest) {
        PaymentMethod paymentMethod = paymentMethodRepository.findById(paymentRequest.getPaymentMethodID()).get();
        Account account = accountRepository.findById(paymentRequest.getAccountID()).get();
        PartyBooking partyBooking = partyBookingRepository.findById(paymentRequest.getBookingID()).get();
        Optional<Payment> existPayment  = paymentRepository.findById(id);
        if (existPayment.isPresent()){
            existPayment.get().setPaymentMethod(paymentMethod == null ? existPayment.get().getPaymentMethod() : paymentMethod);
            existPayment.get().setAccount(account == null ? existPayment.get().getAccount() : account);
            existPayment.get().setPartyBooking(partyBooking == null ? existPayment.get().getPartyBooking() : partyBooking);
            existPayment.get().setUpdateAt(LocalDateTime.now());
            existPayment.get().setStatus(StatusEnum.PENDING);
            LocalDateTime expireDate =  existPayment.get().getUpdateAt().plus(30, ChronoUnit.DAYS);
            existPayment.get().setExpireDate(expireDate);
            paymentRepository.save(existPayment.get());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Update successful", existPayment));
        }
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Payment does not exist", null));

    }

    @Override
    public ResponseEntity<ResponseObj> delete(Long id) {
        Optional<Payment> payment = paymentRepository.findById(id);
        if (payment.isPresent()){
            payment.get().setActive(false);
            payment.get().setDeleteAt(LocalDateTime.now());
            paymentRepository.save(payment.get());
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Delete successful", null));
        }
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Payment does not exist", null));
    }
}

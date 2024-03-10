package com.bookingBirthday.bookingbirthdayforkids.service.impl;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.PaymentMethodRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.request.ServicesRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.model.PartyBooking;
import com.bookingBirthday.bookingbirthdayforkids.model.PaymentMethod;
import com.bookingBirthday.bookingbirthdayforkids.model.Services;
import com.bookingBirthday.bookingbirthdayforkids.repository.PaymentMethodRepository;
import com.bookingBirthday.bookingbirthdayforkids.service.PaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {
    @Autowired
    PaymentMethodRepository paymentMethodRepository;
    @Override
    public ResponseEntity<ResponseObj> getAll(){
        try {
            List<PaymentMethod> paymentMethodList = paymentMethodRepository.findAll();
            if (paymentMethodList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "List is empty", null));
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", paymentMethodList));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> getById(Long id){
        try {
            Optional<PaymentMethod> paymentMethod = paymentMethodRepository.findById(id);
            if(paymentMethod.isPresent())
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), null, paymentMethod));
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Payment method does not exist", null));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> create(PaymentMethodRequest paymentMethodRequest){
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setMethodName(paymentMethodRequest.getMethodName());
        paymentMethod.setMethodDescription(paymentMethodRequest.getMethodDescription());
        paymentMethod.setActive(true);
        paymentMethod.setCreateAt(LocalDateTime.now());
        paymentMethodRepository.save(paymentMethod);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Create successful", paymentMethod));

    }

    public ResponseEntity<ResponseObj> update(Long id, PaymentMethodRequest paymentMethodRequest){
        Optional<PaymentMethod> existPaymentMethod = paymentMethodRepository.findById(id);

        if (existPaymentMethod.isPresent()){
            existPaymentMethod.get().setMethodName(paymentMethodRequest.getMethodName() == null ? existPaymentMethod.get().getMethodName() : paymentMethodRequest.getMethodName());
            existPaymentMethod.get().setMethodDescription(paymentMethodRequest.getMethodDescription() == null ? existPaymentMethod.get().getMethodDescription(): paymentMethodRequest.getMethodDescription());
            existPaymentMethod.get().setUpdateAt(LocalDateTime.now());
            paymentMethodRepository.save(existPaymentMethod.get());

            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Update successful", existPaymentMethod));

        }
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Payment method does not exist", null));

    }

    @Override
    public ResponseEntity<ResponseObj> delete(Long id) {
        Optional<PaymentMethod> paymentMethod = paymentMethodRepository.findById(id);
        if (paymentMethod.isPresent()){
            paymentMethod.get().setDeleteAt(LocalDateTime.now());
            paymentMethod.get().setActive(false);
            paymentMethodRepository.save(paymentMethod.get());
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Delete successful", null));
        }
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Payment method does not exist", null));
    }


}

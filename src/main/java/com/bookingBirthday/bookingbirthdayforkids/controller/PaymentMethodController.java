package com.bookingBirthday.bookingbirthdayforkids.controller;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.PaymentMethodRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.service.PaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment-method")
public class PaymentMethodController {
    @Autowired
    PaymentMethodService paymentMethodService;

    @GetMapping("/getAll-payment-method")
    public ResponseEntity<ResponseObj> getAll(){
        return paymentMethodService.getAll();
    }

    @GetMapping("/getId-payment-method/{id}")
    public ResponseEntity<ResponseObj> getByid(@PathVariable Long id){
        return paymentMethodService.getById(id);
    }

    @PostMapping("/create-payment-method")
    public ResponseEntity<ResponseObj> create(@RequestBody PaymentMethodRequest paymentMethodRequest){
        return  paymentMethodService.create(paymentMethodRequest);
    }

    @PutMapping("/update-payment-method/{id}")
    public ResponseEntity<ResponseObj> update(@PathVariable Long id, @RequestBody PaymentMethodRequest paymentMethodRequest){
        return paymentMethodService.update(id, paymentMethodRequest);
    }

    @DeleteMapping("/delete-payment-method/{id}")
    public ResponseEntity<ResponseObj> delete(@PathVariable Long id){
        return paymentMethodService.delete(id);
    }


}

package com.bookingBirthday.bookingbirthdayforkids.controller;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.PaymentRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    @Autowired
    PaymentService paymentService;

    @GetMapping("/getAll-payment")
    public ResponseEntity<ResponseObj> getAll(){
        return paymentService.getAll();
    }

    @GetMapping("/getId-payment/{id}")
    public ResponseEntity<ResponseObj> getByid(@PathVariable Long id){
        return paymentService.getById(id);
    }

    @PostMapping("/create-payment")
    public ResponseEntity<ResponseObj> create(@RequestBody PaymentRequest paymentRequest){
        return  paymentService.create(paymentRequest);
    }

    @PutMapping("/update-payment/{id}")
    public ResponseEntity<ResponseObj> update(@PathVariable Long id, @RequestBody PaymentRequest paymentRequest){
        return paymentService.update(id, paymentRequest);
    }

    @DeleteMapping("/delete-payment/{id}")
    public ResponseEntity<ResponseObj> delete(@PathVariable Long id){
        return paymentService.delete(id);
    }

}

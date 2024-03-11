package com.bookingBirthday.bookingbirthdayforkids.controller;


import com.bookingBirthday.bookingbirthdayforkids.dto.request.AccountRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.request.InquiryQuestionRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.request.ServicesRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.service.ServicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/services")

public class ServicesController {
    @Autowired
    ServicesService servicesService;

    @GetMapping("/getAll-service")
    public ResponseEntity<ResponseObj> getAll(){
        return servicesService.getAll();
    }

    @GetMapping("/getId-service/{id}")
    public ResponseEntity<ResponseObj> getByid(@PathVariable Long id){
        return servicesService.getById(id);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HOST')")
    @PostMapping("/create-service")
    public ResponseEntity<ResponseObj> create(@RequestBody ServicesRequest servicesRequest){
        return  servicesService.create(servicesRequest);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HOST')")
    @DeleteMapping("/delete-service/{id}")
    public ResponseEntity<ResponseObj> delete(@PathVariable Long id){
        return servicesService.delete(id);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HOST')")
    @PutMapping("/update-service/{id}")
    public ResponseEntity<ResponseObj> update(@PathVariable Long id, @RequestBody ServicesRequest servicesRequest){
        return servicesService.update(id, servicesRequest);
    }
}

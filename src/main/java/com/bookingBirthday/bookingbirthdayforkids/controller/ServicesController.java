package com.bookingBirthday.bookingbirthdayforkids.controller;


import com.bookingBirthday.bookingbirthdayforkids.dto.request.AccountRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.request.InquiryQuestionRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.request.ServicesRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.service.ServicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @PostMapping(value = "/create-service", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> create(@RequestPart(name = "fileImg", required = true) MultipartFile fileImg,
                                    @RequestPart String serviceName,
                                    @RequestPart String description,
                                    @RequestPart String pricing){
        return servicesService.create(fileImg, serviceName, description,Float.parseFloat(pricing));
    }

<<<<<<< HEAD
    @PutMapping("/update-service/{id}")
    public ResponseEntity<ResponseObj> update(@PathVariable Long id, @RequestBody ServicesRequest servicesRequest){
        return servicesService.update(id, servicesRequest);
    }
=======
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HOST')")
>>>>>>> main
    @DeleteMapping("/delete-service/{id}")
    public ResponseEntity<ResponseObj> delete(@PathVariable Long id){
        return servicesService.delete(id);
    }

<<<<<<< HEAD

=======
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HOST')")
    @PutMapping("/update-service/{id}")
    public ResponseEntity<ResponseObj> update(@PathVariable Long id, @RequestBody ServicesRequest servicesRequest){
        return servicesService.update(id, servicesRequest);
    }
>>>>>>> main
}

package com.bookingBirthday.bookingbirthdayforkids.controller;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.PackageServiceRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.service.PackageServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/packageService")
public class PackageServiceController {
    @Autowired
    PackageServiceService packageServiceService;

    @GetMapping("/get-all")
    public ResponseEntity<ResponseObj> getAll(){
        return packageServiceService.getAll();
    }

    @GetMapping("/get-id/{id}")
    public ResponseEntity<ResponseObj> getById(@PathVariable Long id){
        return packageServiceService.getById(id);
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseObj> create(@RequestBody PackageServiceRequest packageServiceRequest){
        return packageServiceService.create(packageServiceRequest);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseObj> update(@PathVariable Long id, @RequestBody PackageServiceRequest packageServiceRequest){
        return packageServiceService.update(id, packageServiceRequest);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseObj> delete(@PathVariable Long id){
        return packageServiceService.delete(id);
    }
}

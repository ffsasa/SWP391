package com.bookingBirthday.bookingbirthdayforkids.controller;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.PackageRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/package")
public class PackageController {
    @Autowired
    PackageService packageService;

    @GetMapping("/get-all")
    public ResponseEntity<ResponseObj> getAll(){ return packageService.getAll();
    }

    @GetMapping("/get-id/{id}")
    public ResponseEntity<ResponseObj> getByid(@PathVariable Long id){
        return packageService.getById(id);
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseObj> create(@RequestBody PackageRequest packageRequest){
        return packageService.create(packageRequest);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseObj> update(@PathVariable Long id, @RequestBody PackageRequest packageRequest){
        return packageService.update(id, packageRequest);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseObj> delete(@PathVariable Long id){
        return packageService.delete(id);
    }
}

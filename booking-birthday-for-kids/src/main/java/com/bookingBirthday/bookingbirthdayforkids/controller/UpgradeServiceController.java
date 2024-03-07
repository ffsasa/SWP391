package com.bookingBirthday.bookingbirthdayforkids.controller;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.UpgradeServiceRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.service.UpgradeServiceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/upgrade-service")
public class UpgradeServiceController {
    @Autowired
    UpgradeServiceService upgradeServiceService;

    @GetMapping("/get-all")
    public ResponseEntity<ResponseObj> getAll() {
        return upgradeServiceService.getAll();
    }

    @GetMapping("/get-id/{id}")
    public ResponseEntity<ResponseObj> getById(@PathVariable Long id){
        return upgradeServiceService.getById(id);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody UpgradeServiceRequest upgradeServiceRequest, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(HttpStatus.BAD_REQUEST.toString());
        return upgradeServiceService.create(upgradeServiceRequest);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,@Valid @RequestBody UpgradeServiceRequest upgradeServiceRequest, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(HttpStatus.BAD_REQUEST.toString());
        return upgradeServiceService.update(id, upgradeServiceRequest);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseObj> delete(@PathVariable Long id){
        return upgradeServiceService.delete(id);
    }
}

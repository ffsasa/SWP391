package com.bookingBirthday.bookingbirthdayforkids.controller;

import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.service.UpgradeServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseObj> delete(@PathVariable Long id){
        return upgradeServiceService.delete(id);
    }
}

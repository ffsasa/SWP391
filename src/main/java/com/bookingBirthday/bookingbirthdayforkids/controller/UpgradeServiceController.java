package com.bookingBirthday.bookingbirthdayforkids.controller;

import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.service.UpgradeServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/get-all-upgrade-service-for-host")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HOST')")
    public ResponseEntity<ResponseObj> getAllForHost() {
        return upgradeServiceService.getAllForHost();
    }

    @GetMapping("/get-id/{id}")
    public ResponseEntity<ResponseObj> getById(@PathVariable Long id){
        return upgradeServiceService.getById(id);
    }

    @GetMapping("/get-upgrade-service-for-customer-id/{id}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<ResponseObj> getById_ForCustomer(@PathVariable Long id){
        return upgradeServiceService.getById_ForCustomer(id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseObj> delete(@PathVariable Long id){
        return upgradeServiceService.delete(id);
    }
}

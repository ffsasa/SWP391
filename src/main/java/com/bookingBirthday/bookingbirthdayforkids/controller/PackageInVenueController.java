package com.bookingBirthday.bookingbirthdayforkids.controller;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.PackageInVenueRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.request.TransactionRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.service.PackageInVenueService;
import com.bookingBirthday.bookingbirthdayforkids.service.VenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/packageInVenue")
public class PackageInVenueController {
    @Autowired
    PackageInVenueService packageInVenueService;

    @GetMapping("/get-all-package-in-venue")
    public ResponseEntity<ResponseObj> getAll() {
        return packageInVenueService.getAll();
    }

    @GetMapping("/get-all-package-in-venue-for-host")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HOST')")
    public ResponseEntity<ResponseObj> getAllForHost() {
        return packageInVenueService.getAllForHost();
    }


    @GetMapping("/get-package-in-venue-id/{id}")
    public ResponseEntity<ResponseObj> getById(@PathVariable Long id){
        return packageInVenueService.getById(id);
    }
    @GetMapping("/get-package-in-venue-id-not-choose/{id}")
    public ResponseEntity<ResponseObj> getPackageInVenueNotChoose(@PathVariable Long id){
        return packageInVenueService.getPackageInVenueNotChoose(id);
    }
    @GetMapping("/get-package-in-venue-for-customer-id/{id}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<ResponseObj> getById_ForCustomer(@PathVariable Long id){
        return packageInVenueService.getById_ForCustomer(id);
    }
    @PutMapping("/update-package-in-venue/{id}")
    public ResponseEntity<ResponseObj> update(@PathVariable Long id, @RequestBody PackageInVenueRequest packageInVenueRequest){
        return packageInVenueService.update(id,packageInVenueRequest);
    }
    @PutMapping("/enable-package-in-venue/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HOST')")
    public ResponseEntity<ResponseObj> enable(@PathVariable Long id){
        return packageInVenueService.activePackageInVenue(id);
    }
    @DeleteMapping("/disable-package-in-venue/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HOST')")
    public ResponseEntity<ResponseObj> delete(@PathVariable Long id){
        return packageInVenueService.delete(id);
    }

}

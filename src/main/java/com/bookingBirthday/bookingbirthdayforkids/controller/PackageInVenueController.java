package com.bookingBirthday.bookingbirthdayforkids.controller;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.PackageInVenueRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.request.TransactionRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.service.PackageInVenueService;
import com.bookingBirthday.bookingbirthdayforkids.service.VenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ResponseObj> getAllForHost() {
        return packageInVenueService.getAllForHost();
    }


    @GetMapping("/get-package-in-venue-id/{id}")
    public ResponseEntity<ResponseObj> getById(@PathVariable Long id){
        return packageInVenueService.getById(id);
    }

    @PutMapping("/update-package-in-venue/{id}")
    public ResponseEntity<ResponseObj> update(@PathVariable Long id, @RequestBody PackageInVenueRequest packageInVenueRequest){
        return packageInVenueService.update(id,packageInVenueRequest);
    }
    @DeleteMapping("/delete-package-in-venue/{id}")
    public ResponseEntity<ResponseObj> delete(@PathVariable Long id){
        return packageInVenueService.delete(id);
    }

}

package com.bookingBirthday.bookingbirthdayforkids.controller;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.PackageInVenueRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.request.ThemeInVenueRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.service.PackageInVenueService;
import com.bookingBirthday.bookingbirthdayforkids.service.ThemeInVenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/themeInVenue")
public class ThemeInVenueController {
    @Autowired
    ThemeInVenueService themeInVenueService;

    @GetMapping("/get-all-theme-in-venue")
    public ResponseEntity<ResponseObj> getAll() {
        return themeInVenueService.getAll();
    }


    @GetMapping("/get-theme-in-venue-id/{id}")
    public ResponseEntity<ResponseObj> getById(@PathVariable Long id){
        return themeInVenueService.getById(id);
    }

    @PutMapping("/update-theme-in-venue/{id}")
    public ResponseEntity<ResponseObj> update(@PathVariable Long id, @RequestBody ThemeInVenueRequest themeInVenueRequest){
        return themeInVenueService.update(id,themeInVenueRequest);
    }
    @DeleteMapping("/delete-theme-in-venue/{id}")
    public ResponseEntity<ResponseObj> delete(@PathVariable Long id){
        return themeInVenueService.delete(id);
    }

}

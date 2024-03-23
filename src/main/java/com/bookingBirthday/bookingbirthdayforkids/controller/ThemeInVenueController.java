package com.bookingBirthday.bookingbirthdayforkids.controller;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.PackageInVenueRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.request.ThemeInVenueRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.service.PackageInVenueService;
import com.bookingBirthday.bookingbirthdayforkids.service.ThemeInVenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/get-all-theme-in-venue-for-host")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HOST')")
    public ResponseEntity<ResponseObj> getAllForHost(){return themeInVenueService.getAll_ForHost();}

    @GetMapping("/get-theme-in-venue-id-not-choose/{id}")
    public ResponseEntity<ResponseObj> getThemeInVenueNotChoose(@PathVariable Long id){
        return themeInVenueService.getThemeInVenueNotChoose(id);
    }
    @GetMapping("/get-theme-in-venue-id/{id}")
    public ResponseEntity<ResponseObj> getById(@PathVariable Long id){
        return themeInVenueService.getById(id);
    }

    @GetMapping("/get-theme-in-venue-for-customer-id/{id}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<ResponseObj> getById_ForCustomer(@PathVariable Long id){
        return themeInVenueService.getById_ForCustomer(id);
    }


    @PutMapping("/update-theme-in-venue/{id}")
    public ResponseEntity<ResponseObj> update(@PathVariable Long id, @RequestBody ThemeInVenueRequest themeInVenueRequest){
        return themeInVenueService.update(id,themeInVenueRequest);
    }
    @PutMapping("/enable-theme-in-venue/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HOST')")
    public ResponseEntity<ResponseObj> enable(@PathVariable Long id){
        return themeInVenueService.activeThemeInVenue(id);
    }
    @DeleteMapping("/disable-theme-in-venue/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HOST')")
    public ResponseEntity<ResponseObj> delete(@PathVariable Long id){
        return themeInVenueService.delete(id);
    }

}

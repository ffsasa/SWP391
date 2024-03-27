package com.bookingBirthday.bookingbirthdayforkids.controller;

import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.service.VenueService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/venue")
public class VenueController {
    @Autowired
    VenueService venueService;

    @GetMapping("/get-all")
    public ResponseEntity<ResponseObj> getAll() {
        return venueService.getAll();
    }

    @GetMapping("/get-all-venue-for-host")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HOST')")
    public ResponseEntity<ResponseObj> getAllForHost() {
        return venueService.getAllForHost();
    }

//    @GetMapping("/check-slot-in-venue")
//    public ResponseEntity<ResponseObj> checkSlotInVenue(@RequestParam String date) {
//        try {
//            return venueService.checkSlotInVenue(LocalDateTime.parse(date));
//        } catch (Exception e) {
//            List<Object> errors = new ArrayList<>();
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Invalid date", errors));
//        }
//    }
//
//    @GetMapping("/check-slot-in-venue-for-host")
//    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HOST')")
//    public ResponseEntity<ResponseObj> checkSlotInVenueForHost(@RequestParam String date) {
//        try {
//            return venueService.checkSlotInVenueForHost(LocalDate.parse(date));
//        } catch (Exception e) {
//            List<Object> errors = new ArrayList<>();
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Invalid date", errors));
//        }
//    }

    @GetMapping("/get-id/{id}")
    public ResponseEntity<ResponseObj> getById(@PathVariable Long id) {
        return venueService.getById(id);
    }

    @GetMapping("/get-venue-for-customer-id/{id}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<ResponseObj> getById_ForCustomer(@PathVariable Long id) {
        return venueService.getById_ForCustomer(id);
    }

    @GetMapping("/get-package-by-venue/{id}")
    public ResponseEntity<ResponseObj> getPackageByVenue(@PathVariable Long id) {
        return venueService.getPackageInVenueByVenue(id);
    }

    @GetMapping("/get-theme-by-venue/{id}")
    public ResponseEntity<ResponseObj> getThemeByVenue(@PathVariable Long id) {
        return venueService.getThemeInVenueByVenue(id);
    }

//    @GetMapping("/get-slot-in-venue-by-venue/{id}")
//    public ResponseEntity<ResponseObj> getSlotInVenueByVenue(@PathVariable Long id) {
//        return venueService.getSlotInVenueById(id);
//    }
//
//    @GetMapping("/get-slot-not-add-in-venue/{id}")
//    public ResponseEntity<ResponseObj> getSlotNotAddInVenue(@PathVariable Long id) {
//        return venueService.getAllSlotHaveNotAddByVenue(id);
//    }
    @GetMapping("/get-theme-not-add-in-venue/{id}")
    public ResponseEntity<ResponseObj> getThemeNotAddInVenue(@PathVariable Long id) {
        return venueService.getAllThemeHaveNotAddByVenue(id);
    }
    @GetMapping("/get-package-not-add-in-venue/{id}")
    public ResponseEntity<ResponseObj> getPackageNotAddInVenue(@PathVariable Long id) {
        return venueService.getAllPackageHaveNotAddByVenune(id);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HOST')")
    @PostMapping(value = "/create-venue", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> create(@RequestPart(name = "fileImg", required = true) MultipartFile fileImg,
                                    @RequestPart String venueName,
                                    @RequestPart String venueDescription,
                                    @RequestPart String location,
                                    @RequestPart String capacity) {
        try {
            int parsedCapacity = Integer.parseInt(capacity);
            return venueService.create(fileImg, venueName, venueDescription, location, parsedCapacity);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Invalid capacity", null));
        }
    }

//    @PutMapping("/set-active-venue/{id}")
//    public ResponseEntity<ResponseObj> setActiveVenue(@PathVariable Long id){
//        return venueService.activeVenue(id);
//    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HOST')")
    @PutMapping(value = "/update-venue/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestPart(name = "fileImg", required = false) MultipartFile fileImg,
                                    @RequestPart String venueName,
                                    @RequestPart String venueDescription,
                                    @RequestPart String location,
                                    @RequestPart String capacity) {
        try {
            int parsedCapacity = Integer.parseInt(capacity);
            return venueService.update(id, fileImg, venueName, venueDescription, location, parsedCapacity);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Invalid capacity", null));
        }
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HOST')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseObj> delete(@PathVariable Long id) {
        return venueService.delete(id);
    }

//    @PostMapping("/add-theme")
//    public ResponseEntity<ResponseObj> addTheme(@RequestParam Long venueId, @RequestParam Long themeId) {
//        return venueService.addTheme(venueId, themeId);
//    }
//
//    @PostMapping("/add-package")
//    public ResponseEntity<ResponseObj> addPackage(@RequestParam Long venueId, @RequestParam Long packageId) {
//        return venueService.addPackage(venueId, packageId);
//    }
}

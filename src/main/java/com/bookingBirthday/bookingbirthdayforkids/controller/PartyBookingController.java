package com.bookingBirthday.bookingbirthdayforkids.controller;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.PartyBookingRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.model.PartyBooking;
import com.bookingBirthday.bookingbirthdayforkids.service.PartyBookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/party-booking")
public class PartyBookingController {

    @Autowired
    PartyBookingService partyBookingService;

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @GetMapping("/get-all-by-user")
    public ResponseEntity<ResponseObj> getAllByUser() {
        return partyBookingService.getAllByUser();
    }

    @GetMapping("/get-all-party-booking-for-host/{venueId}")
    @PreAuthorize("hasAuthority('HOST')")
    public ResponseEntity<ResponseObj> getAllForHost(@PathVariable Long venueId) {
        return partyBookingService.getAll_ForHost(venueId);
    }

    @GetMapping("/get-all-completed")
    @PreAuthorize("hasAuthority('HOST')")
    public ResponseEntity<ResponseObj> getAllCompleted() {
        return partyBookingService.getAllCompleted();
    }

    @GetMapping("/get-id/{id}")
    public ResponseEntity<ResponseObj> getById(@PathVariable Long id){
        return partyBookingService.getById(id);
    }

//    @GetMapping("/get-party-booking-for-customer-id/{id}")
//    @PreAuthorize("hasAuthority('CUSTOMER')")
//    public ResponseEntity<ResponseObj> getById_ForCustomer(@PathVariable Long id){
//        return partyBookingService.getById_ForCustomer(id);
//    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody PartyBookingRequest partyBookingRequest){
        return partyBookingService.create(partyBookingRequest);
    }
//
//    @PutMapping("/update/{id}")
//    public ResponseEntity<?> update(@PathVariable Long id,@Valid @RequestBody PartyBookingRequest partyBookingRequest){
//        return partyBookingService.update(id, partyBookingRequest);
//    }

    @PutMapping("/party-booking-cancel/{bookingId}")
    public ResponseEntity<ResponseObj> Cancel(@PathVariable Long bookingId){
        return partyBookingService.Cancel(bookingId);
    }
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HOST')")
    @PutMapping("/party-booking-cancel-for-host/{bookingId}")
    public ResponseEntity<ResponseObj> cancelBookingForHost(@PathVariable Long bookingId) {
        return partyBookingService.cancelBookingForHost(bookingId);
    }

    @PutMapping("/party-booking-cancel-for-customer/{bookingId}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<ResponseObj> cancelBookingForCustomer(@PathVariable Long bookingId) {
        return partyBookingService.cancelBookingForCustomer(bookingId);
    }
    @PutMapping("/complete-booking-for-host/{bookingId}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HOST')")
    public ResponseEntity<ResponseObj> completeBookingForHost(@PathVariable Long bookingId) {
        return partyBookingService.completeBookingForHost(bookingId);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseObj> delete(@PathVariable Long id){
        return partyBookingService.delete(id);
    }

    @PatchMapping("/update-package-in-venue/{partyBookingId}/{packageInVenueId}")
    public ResponseEntity<ResponseObj> updatePackageInVenue(@PathVariable Long partyBookingId, @PathVariable Long packageInVenueId){
        return partyBookingService.updatePackageInVenue(partyBookingId, packageInVenueId);
    }
}

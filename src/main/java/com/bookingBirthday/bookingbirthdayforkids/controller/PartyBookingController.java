package com.bookingBirthday.bookingbirthdayforkids.controller;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.PartyBookingRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.service.PartyBookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/party-booking")
public class PartyBookingController {

    @Autowired
    PartyBookingService partyBookingService;

    @GetMapping("/get-all")
    public ResponseEntity<ResponseObj> getAll() {
        return partyBookingService.getAll();
    }
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @GetMapping("/get-all-by-user")
    public ResponseEntity<ResponseObj> getAllByUser() {
        return partyBookingService.getAllByUser();
    }

    @GetMapping("/get-id/{id}")
    public ResponseEntity<ResponseObj> getById(@PathVariable Long id){
        return partyBookingService.getById(id);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody PartyBookingRequest partyBookingRequest, BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            List<Object> errors = new ArrayList<>();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Invalid input", errors));
        }
        return partyBookingService.create(partyBookingRequest);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,@Valid @RequestBody PartyBookingRequest partyBookingRequest, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(HttpStatus.BAD_REQUEST.toString());
        return partyBookingService.update(id, partyBookingRequest);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseObj> delete(@PathVariable Long id){
        return partyBookingService.delete(id);
    }
}

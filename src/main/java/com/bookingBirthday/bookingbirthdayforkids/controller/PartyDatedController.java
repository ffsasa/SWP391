package com.bookingBirthday.bookingbirthdayforkids.controller;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.PartyDatedRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.service.PartyDatedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/partyDated")
public class PartyDatedController {
    @Autowired
    PartyDatedService partyDatedService;

    @GetMapping("/get-all")
    public ResponseEntity<ResponseObj> getAll(){
        return partyDatedService.getAll();
    }

    @GetMapping("/get-all-for-host")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HOST')")
    public ResponseEntity<ResponseObj> getAllForHost(){
        return partyDatedService.getAllForHost();
    }


    @GetMapping("/get-id/{id}")
    public ResponseEntity<ResponseObj> getById(@PathVariable Long id){
        return partyDatedService.getById(id);
    }

    @GetMapping("/get-party-booking/{id}")
    public ResponseEntity<ResponseObj> getPartyBookingByPartyDateId(@PathVariable Long id){
        return partyDatedService.getPartyBookingByPartyDateId(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseObj> update(@PathVariable Long id, @RequestBody PartyDatedRequest partyDatedRequest){
        return partyDatedService.update(id, partyDatedRequest);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseObj> delete(@PathVariable Long id){
        return partyDatedService.delete(id);
    }
}

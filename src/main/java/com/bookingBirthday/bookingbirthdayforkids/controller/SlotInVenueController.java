package com.bookingBirthday.bookingbirthdayforkids.controller;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.SlotInVenueRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.service.SlotInVenueService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/slot-in-venue")
public class SlotInVenueController {
    @Autowired
    SlotInVenueService slotinVenueService;

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HOST')")
    @PostMapping("/create")
    public ResponseEntity<ResponseObj> create(@Valid @RequestBody SlotInVenueRequest slotInVenueRequest){
        return slotinVenueService.create(slotInVenueRequest);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HOST')")
    @DeleteMapping("/disable/{id}")
    public ResponseEntity<ResponseObj> disableSlotInVenue(@PathVariable Long id){
        return slotinVenueService.disableSlotInVenue(id);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HOST')")
    @PutMapping("/active/{id}")
    public ResponseEntity<ResponseObj> activeSlotInVenue(@PathVariable Long id){
        return slotinVenueService.activeSlotInVenue(id);
    }
}

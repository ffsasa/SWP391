package com.bookingBirthday.bookingbirthdayforkids.controller;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.SlotInVenueRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.service.SlotInVenueService;
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
    public ResponseEntity<ResponseObj> create(@RequestBody SlotInVenueRequest slotInVenueRequest){
        return slotinVenueService.create(slotInVenueRequest);
    }
}

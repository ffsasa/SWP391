package com.bookingBirthday.bookingbirthdayforkids.controller;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.SlotInVenueRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.service.SlotInVenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/slot-in-venue")
public class SlotInVenueController {
    @Autowired
    SlotInVenueService slotinVenueService;
    @PostMapping("/create")
    public ResponseEntity<ResponseObj> create(@RequestBody SlotInVenueRequest slotInVenueRequest){
        return slotinVenueService.create(slotInVenueRequest);
    }
    @GetMapping("/get-all")
    public ResponseEntity<ResponseObj> getAll(){
        return slotinVenueService.getAll();
    }
}

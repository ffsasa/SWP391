package com.bookingBirthday.bookingbirthdayforkids.controller;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.SlotRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.service.SlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/slot")
public class SlotController {
    @Autowired
    SlotService slotService;

    @GetMapping("/get-all")
    public ResponseEntity<ResponseObj> getAll(){
        return slotService.getAll();
    }

    @GetMapping("/get-all-for-host")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HOST')")
    public ResponseEntity<ResponseObj> getAllForHost(){
        return slotService.getAllForHost();
    }

    @GetMapping("/get-id/{id}")
    public ResponseEntity<ResponseObj> getById(@PathVariable Long id){
        return slotService.getById(id);
    }

    @GetMapping("/get-slot-for-customer-id/{id}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<ResponseObj> getById_ForCustomer(@PathVariable Long id){
        return slotService.getById_ForCustomer(id);
    }
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HOST')")
    @PostMapping("/create")
    public ResponseEntity<ResponseObj> create(@RequestBody SlotRequest slotRequest){
        return slotService.create(slotRequest);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HOST')")
    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseObj> update(@PathVariable Long id, @RequestBody SlotRequest slotRequest){
        return slotService.update(id, slotRequest);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HOST')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseObj> delete(@PathVariable Long id){
        return slotService.delete(id);
    }
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HOST')")
    @PostMapping(value = "/add-slot-in-venue-by-venue-id")
    public ResponseEntity<?> addSlotInVenueBySlotId(@RequestParam Long venueId, @RequestBody List<Long> slotId){
        return slotService.addSlotInVenueByVenueId(venueId, slotId);
    }
}

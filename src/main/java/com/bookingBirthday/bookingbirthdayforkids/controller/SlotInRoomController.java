package com.bookingBirthday.bookingbirthdayforkids.controller;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.SlotInRoomRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.service.SlotInRoomService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/slot-in-venue")
public class SlotInRoomController {
    @Autowired
    SlotInRoomService slotinRoomService;

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HOST')")
    @PostMapping("/create")
    public ResponseEntity<ResponseObj> create(@Valid @RequestBody SlotInRoomRequest slotInRoomRequest){
        return slotinRoomService.create(slotInRoomRequest);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HOST')")
    @DeleteMapping("/disable/{id}")
    public ResponseEntity<ResponseObj> disableSlotInVenue(@PathVariable Long id){
        return slotinRoomService.disableSlotInRoom(id);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HOST')")
    @PutMapping("/active/{id}")
    public ResponseEntity<ResponseObj> activeSlotInVenue(@PathVariable Long id){
        return slotinRoomService.activeSlotInRoom(id);
    }
}

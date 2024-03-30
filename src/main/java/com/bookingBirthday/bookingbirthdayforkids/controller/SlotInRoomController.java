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

    @PreAuthorize("hasAuthority('HOST')")
    @PostMapping("/create/{venueId}")
    public ResponseEntity<ResponseObj> create(@PathVariable Long venueId, @Valid @RequestBody SlotInRoomRequest slotInRoomRequest){
        return slotinRoomService.create(slotInRoomRequest, venueId);
    }

    @PreAuthorize("hasAuthority('HOST')")
    @DeleteMapping("/disable/{slotInRoomId}")
    public ResponseEntity<ResponseObj> disableSlotInVenue(@PathVariable Long slotInRoomId){
        return slotinRoomService.disableSlotInRoom(slotInRoomId);
    }

    @PreAuthorize("hasAuthority('HOST')")
    @PutMapping("/active/{slotInRoomId}")
    public ResponseEntity<ResponseObj> activeSlotInVenue(@PathVariable Long slotInRoomId){
        return slotinRoomService.activeSlotInRoom(slotInRoomId);
    }
}

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

    @GetMapping("/get-all-slot-for-customer")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<ResponseObj> getAllSlotForCustomer() {
        return slotService.getAllSlotForCustomer();
    }

    @GetMapping("/get-all-slot-for-host")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HOST')")
    public ResponseEntity<ResponseObj> getAllSlotForHost() {
        return slotService.getAllSlotForHost();
    }

    @GetMapping("/get-id-for-customer/{id}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<ResponseObj> getByIdForCustomer(@PathVariable Long id) {
        return slotService.getByIdForCustomer(id);
    }

    @GetMapping("/get-id-for-host/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HOST')")
    public ResponseEntity<ResponseObj> getByIdForHost(@PathVariable Long id) {
        return slotService.getByIdForHost(id);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HOST')")
    @PostMapping("/create")
    public ResponseEntity<ResponseObj> create(@RequestBody SlotRequest slotRequest) {
        return slotService.create(slotRequest);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HOST')")
    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseObj> update(@PathVariable Long id, @RequestBody SlotRequest slotRequest) {
        return slotService.update(id, slotRequest);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HOST')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseObj> delete(@PathVariable Long id) {
        return slotService.delete(id);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HOST')")
    @PostMapping(value = "/add-slot-in-room-by-slot-id")
    public ResponseEntity<?> addSlotInRoomBySlotId(@RequestParam Long roomId, @RequestBody List<Long> slotId) {
        return slotService.addSlotInRoomByRoomId(roomId, slotId);
    }
}

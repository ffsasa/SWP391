package com.bookingBirthday.bookingbirthdayforkids.controller;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.RoomRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/room")
public class RoomController {

    @Autowired
    RoomService roomService;

    @GetMapping("/get-all-in-venue-for-customer/{venueId}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<ResponseObj> getAllForCustomer(@PathVariable Long venueId) {
        return roomService.getAllRoomInVenueByCustomer(venueId);
    }

    @GetMapping("/get-room-in-venue-by-id-for-customer/{roomId}/{venueId}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<ResponseObj> getByIdForCustomer(@PathVariable Long roomId, @PathVariable Long venueId) {
        return roomService.getRoomInVenueByIdForCustomer(roomId, venueId);
    }

    @GetMapping("get-slot-in-room-in-venue-by-customer/{roomId}/{venueId}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<ResponseObj> getSlotInRoomInVenueForCustomer(@PathVariable Long roomId, @PathVariable Long venueId) {
        return roomService.getSlotInRoomByIdForCustomer(roomId, venueId);
    }

    @GetMapping("/get-all-in-venue-for-host")
    @PreAuthorize("hasAuthority('HOST')")
    public ResponseEntity<ResponseObj> getAllRoomInVenueForHost(@RequestParam(required = false, defaultValue = "") String active) {
        if(active.isEmpty()){
            return roomService.getAllRoomInVenueByHost();
        }else{
            Boolean isActive = Boolean.parseBoolean(active);
            if(isActive){
                return roomService.getAllRoomInVenueIsTrueByHost();
            }
            return roomService.getAllRoomInVenueIsFalseByHost();
        }
    }

    @GetMapping("/get-room-in-venue-by-id-for-host/{roomId}")
    @PreAuthorize("hasAuthority('HOST')")
    public ResponseEntity<ResponseObj> getByIdForHost(@PathVariable Long roomId) {
        return roomService.getRoomInVenueByIdForHost(roomId);
    }

    @GetMapping("get-slot-in-rom-in-venue-by-host/{roomId}")
    @PreAuthorize("hasAuthority('HOST')")
    public ResponseEntity<ResponseObj> getSlotInRoomInVenueForHost(@PathVariable Long roomId) {
        return roomService.getSlotInRoomByIdForHost(roomId);
    }

    @GetMapping("get-slot-not-add-in-room-in-venue-for-host/{roomId}")
    @PreAuthorize("hasAuthority('HOST')")
    public ResponseEntity<ResponseObj> getSlotNotAddInRoomInVenueForHost(@PathVariable Long roomId) {
        return roomService.getSlotNotAddInRoomByIdForHost(roomId);
    }

    @GetMapping("get-slot-in-room-in-venue-by-id-for-host/{roomId}")
    @PreAuthorize("hasAuthority('HOST')")
    public ResponseEntity<ResponseObj> getSlotInInRoomInVenueByIdForHost(@PathVariable Long roomId) {
        return roomService.getSlotInRoomByIdForHost(roomId);
    }

    @GetMapping("check-slot-in-room-for-customer/{venueId}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<ResponseObj> checkSlotInRoomForCustomer(@RequestParam String date, @PathVariable Long venueId) {
        LocalDate parseDate = LocalDate.parse(date);
        return roomService.checkSlotInRoomForCustomer(parseDate, venueId);
    }

    @GetMapping("check-slot-in-room-for-host")
    @PreAuthorize("hasAuthority('HOST')")
    public ResponseEntity<ResponseObj> checkSlotInRoomForHost(@RequestParam String date) {
        LocalDate parseDate = LocalDate.parse(date);
        return roomService.checkSlotInRoomForHost(parseDate);
    }

    @PostMapping(value = "/create-room", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('HOST')")
    public ResponseEntity<?> create(
            @RequestPart(name = "fileImg", required = true) MultipartFile fileImg,
            @RequestPart String roomName,
            @RequestPart String capacity,
            @RequestPart String pricing) {
        try {
            float parsedPricing = Float.parseFloat(pricing);
            int parsedCapacity = Integer.parseInt(capacity);
            return roomService.create(fileImg, roomName, parsedCapacity, parsedPricing);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Invalid pricing", null));
        }
    }

    @PutMapping("/update/{roomId}")
    @PreAuthorize("hasAuthority('HOST')")
    public ResponseEntity<ResponseObj> update(@PathVariable Long roomId, @RequestPart(name = "fileImg", required = false) MultipartFile fileImg,
                                              @RequestPart String roomName,
                                              @RequestPart String capacity,
                                              @RequestPart String pricing) {
        try {
            float parsedPricing = Float.parseFloat(pricing);
            int parsedCapacity = Integer.parseInt(capacity);
            return roomService.update(roomId, fileImg, roomName, parsedCapacity, parsedPricing);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Invalid pricing", null));
        }
    }

    @PutMapping("enable/{roomId}")
    @PreAuthorize("hasAuthority('HOST')")
    public ResponseEntity<ResponseObj> enable(@PathVariable Long roomId) {
        return roomService.enable(roomId);
    }

    @DeleteMapping("/disable/{roomId}")
    @PreAuthorize("hasAuthority('HOST')")
    public ResponseEntity<ResponseObj> delete(@PathVariable Long roomId) {
        return roomService.delete(roomId);
    }
}

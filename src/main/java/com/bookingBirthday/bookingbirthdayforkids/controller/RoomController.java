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

    @GetMapping("get-slot-in-rom-in-venue-by-customer/{roomId}/{venueId}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<ResponseObj> getSlotInRoomInVenueForCustomer(@PathVariable Long roomId, @PathVariable Long venueId) {
        return roomService.getSlotInRoomByIdForCustomer(roomId, venueId);
    }

    @GetMapping("/get-all-in-venue-for-host/{venueId}")
    @PreAuthorize("hasAuthority('HOST')")
    public ResponseEntity<ResponseObj> getAllRoomInVenueForHost(@PathVariable Long venueId) {
        return roomService.getAllRoomInVenueByHost(venueId);
    }

    @GetMapping("/get-room-in-venue-by-id-for-host/{venueId}/{roomId}")
    @PreAuthorize("hasAuthority('HOST')")
    public ResponseEntity<ResponseObj> getByIdForHost(@PathVariable Long venueId, @PathVariable Long roomId) {
        return roomService.getRoomInVenueByIdForHost(roomId, venueId);
    }

    @GetMapping("get-slot-in-rom-in-venue-by-host/{venueId}/{roomId}")
    @PreAuthorize("hasAuthority('HOST')")
    public ResponseEntity<ResponseObj> getSlotInRoomInVenueForHost(@PathVariable Long venueId, @PathVariable Long roomId) {
        return roomService.getSlotInRoomByIdForHost(roomId, venueId);
    }

    @GetMapping("get-slot-not-add-in-room-in-venue-for-host/{venueId}/{roomId}")
    @PreAuthorize("hasAuthority('HOST')")
    public ResponseEntity<ResponseObj> getSlotNotAddInRoomInVenueForHost(@PathVariable Long venueId, @PathVariable Long roomId) {
        return roomService.getSlotNotAddInRoomByIdForHost(roomId, venueId);
    }

    @GetMapping("get-slot-in-room-in-venue-by-id-for-host/{venueId}/{roomId}")
    @PreAuthorize("hasAuthority('HOST')")
    public ResponseEntity<ResponseObj> getSlotInInRoomInVenueByIdForHost(@PathVariable Long venueId, @PathVariable Long roomId) {
        return roomService.getSlotInRoomByIdForHost(roomId, venueId);
    }

    @GetMapping("get-slot-in-room-in-venue-by-id-for-customer/{venueId}/{roomId}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<ResponseObj> getSlotInInRoomInVenueByIdForCustomer(@PathVariable Long venueId, @PathVariable Long roomId) {
        return roomService.getSlotInRoomByIdForCustomer(roomId, venueId);
    }

    @GetMapping("check-slot-in-room-for-customer/{date}/{venueId}")
    @PreAuthorize("hasAuthority('HOST')")
    public ResponseEntity<ResponseObj> checkSlotInRoomForHost(@RequestPart String date, @PathVariable Long venueId) {
        LocalDateTime parseDate = LocalDateTime.parse(date);
        return roomService.checkSlotInRoomForCustomer(parseDate, venueId);
    }

    @GetMapping("check-slot-in-room-for-host/{date}")
    @PreAuthorize("hasAuthority('HOST')")
    public ResponseEntity<ResponseObj> checkSlotInRoomForHost(@RequestPart String date) {
        LocalDate parseDate = LocalDate.parse(date);
        return roomService.checkSlotInRoomForHost(parseDate);
    }


    @PreAuthorize("hasAuthority('HOST')")
    @PostMapping(value = "/create-room/venueId{}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> create(@PathVariable Long venueId,
                                    @RequestPart(name = "fileImg", required = true) MultipartFile fileImg,
                                    @RequestPart String roomName,
                                    @RequestPart String capacity,
                                    @RequestPart String pricing) {
        try {
            float parsedPricing = Float.parseFloat(pricing);
            int parsedCapacity = Integer.parseInt(capacity);
            return roomService.create(fileImg, roomName, venueId, parsedCapacity, parsedPricing);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Invalid pricing", null));
        }
    }

    @PutMapping("/update/{venueId}/{roomId}")
    @PreAuthorize("hasAuthority('HOST')")
    public ResponseEntity<ResponseObj> update(@PathVariable Long venueId, @PathVariable Long roomId, @RequestPart(name = "fileImg", required = false) MultipartFile fileImg,
                                              @RequestPart String roomName,
                                              @RequestPart String capacity,
                                              @RequestPart String pricing) {
        try {
            float parsedPricing = Float.parseFloat(pricing);
            int parsedCapacity = Integer.parseInt(capacity);
            return roomService.update(roomId, venueId, fileImg, roomName, parsedCapacity, parsedPricing);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Invalid pricing", null));
        }
    }

    @DeleteMapping("/delete/{venueId}/{roomId}")
    @PreAuthorize("hasAuthority('HOST')")
    public ResponseEntity<ResponseObj> delete(@PathVariable Long venueId, @PathVariable Long roomId) {
        return roomService.delete(roomId, venueId);
    }
}

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

@RestController
@RequestMapping("/api/room")
public class RoomController {

    @Autowired
    RoomService roomService;

    @GetMapping("/get-all")
    public ResponseEntity<ResponseObj> getAll(){
        return roomService.getAll();
    }

    @GetMapping("/get-id/{id}")
    public ResponseEntity<ResponseObj> getById(@PathVariable Long id){
        return roomService.getById(id);
    }

    @GetMapping("get-slot-not-add-in-rom/{id}")
    public ResponseEntity<ResponseObj> getSlotNotAddInRoom(@PathVariable Long id){
        return roomService.getSlotNotAddInRoomById(id);
    }

    @GetMapping("check-slot-in-room-for-host{date}")
    @PreAuthorize("hasAuthority('HOST')")
    public  ResponseEntity<ResponseObj> checkSlotInRoomForHost(@RequestPart String date){
        LocalDate parseDate = LocalDate.parse(date);
        return roomService.checkSlotInRoomForHost(parseDate);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HOST')")
    @PostMapping(value = "/create-room", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> create(@RequestPart(name = "fileImg", required = true) MultipartFile fileImg,
                                    @RequestPart String roomName,
                                    @RequestPart String venueId,
                                    @RequestPart String capacity,
                                    @RequestPart String pricing) {
        try {
            float parsedPricing = Float.parseFloat(pricing);
            Long parsedVenueId = Long.parseLong(venueId);
            int parsedCapacity = Integer.parseInt(capacity);
            return roomService.create(fileImg, roomName, parsedVenueId, parsedCapacity, parsedPricing);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Invalid pricing", null));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseObj> update(@PathVariable Long id,@RequestPart(name = "fileImg", required = false ) MultipartFile fileImg,
                                              @RequestPart String roomName,
                                              @RequestPart String capacity,
                                              @RequestPart String pricing) {
            try {
                float parsedPricing = Float.parseFloat(pricing);
                int parsedCapacity = Integer.parseInt(capacity);
                return roomService.update(id, fileImg, roomName,parsedCapacity, parsedPricing);
            } catch (NumberFormatException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Invalid pricing", null));
            }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseObj> delete(@PathVariable Long id) {
        return roomService.delete(id);
    }
}

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

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HOST')")
    @PostMapping(value = "/create-room", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> create(@RequestPart(name = "fileImg", required = true) MultipartFile fileImg,
                                    @RequestPart String roomName,
                                    @RequestPart String venueId,
                                    @RequestPart String capacity,
                                    @RequestPart String pricing){
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
    public ResponseEntity<ResponseObj> update(@PathVariable Long id, @RequestBody RoomRequest roomRequest) {
        return roomService.update(id, roomRequest);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseObj> delete(@PathVariable Long id) {
        return roomService.delete(id);
    }
}

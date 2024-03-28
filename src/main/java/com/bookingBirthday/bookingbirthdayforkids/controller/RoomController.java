package com.bookingBirthday.bookingbirthdayforkids.controller;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.RoomRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/get-slot-not-add-in-room/{id}")
    public ResponseEntity<ResponseObj> getSlotNotAddInRoom(@PathVariable Long id) {
        return roomService.getSlotNotAddInRoomById(id);
    }

    @GetMapping("/get-slot-in-room/{id}")
    public ResponseEntity<ResponseObj> getSlotAddInRoom(@PathVariable Long id) {
        return roomService.getSlotInRoomById(id);
    }
    @PostMapping("/create")
    public ResponseEntity<ResponseObj> create(@RequestBody RoomRequest roomRequest) {
        return roomService.create(roomRequest);
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

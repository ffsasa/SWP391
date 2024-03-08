package com.bookingBirthday.bookingbirthdayforkids.controller;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.SlotRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.service.SlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/slot")
public class SlotController {
    @Autowired
    SlotService slotService;

    @GetMapping("/get-all")
    public ResponseEntity<ResponseObj> getAll(){
        return slotService.getAll();
    }

    @GetMapping("/get-id/{id}")
    public ResponseEntity<ResponseObj> getById(@PathVariable Long id){
        return slotService.getById(id);
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseObj> create(@RequestBody SlotRequest slotRequest){
        return slotService.create(slotRequest);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseObj> update(@PathVariable Long id, @RequestBody SlotRequest slotRequest){
        return slotService.update(id, slotRequest);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseObj> delete(@PathVariable Long id){
        return slotService.delete(id);
    }
}

package com.bookingBirthday.bookingbirthdayforkids.controller;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.PartyDatedRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.service.PartyDatedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/partyDated")
public class PartyDatedController {
    @Autowired
    PartyDatedService partyDatedService;

    @GetMapping("/get-all")
    public ResponseEntity<ResponseObj> getAll(){
        return partyDatedService.getAll();
    }

    @GetMapping("/get-id/{id}")
    public ResponseEntity<ResponseObj> getById(@PathVariable Long id){
        return partyDatedService.getById(id);
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseObj> create(@RequestBody PartyDatedRequest partyDatedRequest){
        return partyDatedService.create(partyDatedRequest);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseObj> update(@PathVariable Long id, @RequestBody PartyDatedRequest partyDatedRequest){
        return partyDatedService.update(id, partyDatedRequest);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseObj> delete(@PathVariable Long id){
        return partyDatedService.delete(id);
    }
}

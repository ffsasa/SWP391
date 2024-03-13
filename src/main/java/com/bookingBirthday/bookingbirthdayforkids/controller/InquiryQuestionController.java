package com.bookingBirthday.bookingbirthdayforkids.controller;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.InquiryQuestionRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.service.InquiryQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inquiry")
@CrossOrigin(origins = {"http://Localhost:3000", "https://localhost:8080"}, allowCredentials = "true")
@PreAuthorize("hasAuthority('CUSTOMER')")
public class InquiryQuestionController {
    @Autowired
    InquiryQuestionService inquiryService;

    @PostMapping("/create-question")
    public ResponseEntity<ResponseObj> create(@RequestBody InquiryQuestionRequest inquiryRequest){
        return inquiryService.create(inquiryRequest);
    }

    @GetMapping("/get-all-question")
    public ResponseEntity<ResponseObj> getAll(){
        return inquiryService.getAll();
    }

    @GetMapping("/get-question-by-id/{id}")
    public ResponseEntity<ResponseObj> getById(@PathVariable Long id){
        return inquiryService.getById(id);
    }

    @DeleteMapping("/delete-question/{id}")
    public ResponseEntity<ResponseObj> delete(@PathVariable Long id){
        return inquiryService.delete(id);
    }

    @PutMapping("/update-question/{id}")
    public ResponseEntity<ResponseObj> update(@PathVariable Long id, @RequestBody InquiryQuestionRequest inquiryRequest){
        return inquiryService.update(id, inquiryRequest);
    }
}

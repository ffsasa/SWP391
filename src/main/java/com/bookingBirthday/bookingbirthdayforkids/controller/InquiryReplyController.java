package com.bookingBirthday.bookingbirthdayforkids.controller;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.InquiryReplyRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.service.InquiryReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inquiry")
@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HOST')")
public class InquiryReplyController {
    @Autowired
    InquiryReplyService inquiryReplyService;
    @PutMapping("/reply/{id}")
    public ResponseEntity<ResponseObj> reply(@PathVariable Long id, @RequestBody InquiryReplyRequest inquiryReplyRequest){
        return inquiryReplyService.reply(id, inquiryReplyRequest);
    }
    @GetMapping("/get-all")
    public ResponseEntity<ResponseObj> getAll(){
        return inquiryReplyService.getAll();
    }
}

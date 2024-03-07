package com.bookingBirthday.bookingbirthdayforkids.service;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.InquiryReplyRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import org.springframework.http.ResponseEntity;

public interface InquiryReplyService {
    ResponseEntity<ResponseObj> reply(Long id, InquiryReplyRequest inquiryReplyRequest);
    ResponseEntity<ResponseObj> getAll();
}

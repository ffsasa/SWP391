package com.bookingBirthday.bookingbirthdayforkids.service;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.InquiryQuestionRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import org.springframework.http.ResponseEntity;

public interface InquiryQuestionService {
    public ResponseEntity<ResponseObj> create(InquiryQuestionRequest inquiryRequest);
    public ResponseEntity<ResponseObj> getById(Long id);
    public ResponseEntity<ResponseObj> getAll();
    public ResponseEntity<ResponseObj> delete(Long id);
    public ResponseEntity<ResponseObj> update(Long id, InquiryQuestionRequest inquiryRequest);
    public ResponseEntity<ResponseObj> sendInquiryForChangePackageInVenue(Long bookingId, Long packageInVenueId);

}

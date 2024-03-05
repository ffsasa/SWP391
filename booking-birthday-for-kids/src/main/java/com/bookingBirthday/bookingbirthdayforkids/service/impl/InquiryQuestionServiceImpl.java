package com.bookingBirthday.bookingbirthdayforkids.service.impl;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.InquiryQuestionRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.model.Account;
import com.bookingBirthday.bookingbirthdayforkids.model.Inquiry;
import com.bookingBirthday.bookingbirthdayforkids.repository.AccountRepository;
import com.bookingBirthday.bookingbirthdayforkids.repository.InquiryRepository;
import com.bookingBirthday.bookingbirthdayforkids.service.InquiryQuestionService;
import com.bookingBirthday.bookingbirthdayforkids.util.AuthenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class InquiryQuestionServiceImpl implements InquiryQuestionService {
    @Autowired
    InquiryRepository inquiryRepository;
    @Autowired
    AccountRepository accountRepository;
    @Override
    public ResponseEntity<ResponseObj> create(InquiryQuestionRequest inquiryRequest) {
        Long userId = AuthenUtil.getCurrentUserId();
        if(userId == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "400", null));
        }
        Account account = accountRepository.findById(userId).get();
        Inquiry inquiry = new Inquiry();
        inquiry.setInquiryQuestion(inquiryRequest.getInquiryQuestion());
        inquiry.setActive(true);
        inquiry.setCreateAt(LocalDateTime.now());
        inquiry.setUpdateAt(LocalDateTime.now());
        inquiry.setAccount(account);
        inquiryRepository.save(inquiry);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseObj(HttpStatus.CREATED.toString(), "Create Inquiry Successful", inquiryRequest));
    }

    @Override
    public ResponseEntity<ResponseObj> update(Long id, InquiryQuestionRequest inquiryRequest) {
        Optional<Inquiry> inquiry = inquiryRepository.findById(id);

        if (inquiry.isPresent()){
            inquiry.get().setInquiryQuestion(inquiryRequest.getInquiryQuestion() == null ? inquiry.get().getInquiryQuestion() : inquiryRequest.getInquiryQuestion());
            inquiry.get().setUpdateAt(LocalDateTime.now());
            inquiryRepository.save(inquiry.get());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Update successful", inquiry));
        }

        return null;
    }

    @Override
    public ResponseEntity<ResponseObj> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "List", inquiryRepository.findAllByIsActiveIsTrue()));
    }

    @Override
    public ResponseEntity<ResponseObj> delete(Long id) {
        Optional<Inquiry> inquiry = inquiryRepository.findById(id);
        if (inquiry.isPresent()){
            inquiry.get().setActive(false);
            inquiry.get().setUpdateAt(LocalDateTime.now());
            inquiry.get().setDeleteAt(LocalDateTime.now());
            inquiryRepository.save(inquiry.get());
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Delete successful", null));
        }
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Inquiry does not exist", null));
    }

    @Override
    public ResponseEntity<ResponseObj> getById(Long id) {
        Optional<Inquiry> inquiry = inquiryRepository.findById(id);
        if(inquiry.isPresent())
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Inquiry", inquiry));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Inquiry does not exist", null));
    }
}

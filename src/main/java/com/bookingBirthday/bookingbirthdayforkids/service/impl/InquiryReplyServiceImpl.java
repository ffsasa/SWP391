package com.bookingBirthday.bookingbirthdayforkids.service.impl;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.InquiryReplyRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.model.Account;
import com.bookingBirthday.bookingbirthdayforkids.model.Inquiry;
import com.bookingBirthday.bookingbirthdayforkids.repository.AccountRepository;
import com.bookingBirthday.bookingbirthdayforkids.repository.InquiryRepository;
import com.bookingBirthday.bookingbirthdayforkids.service.InquiryReplyService;
import com.bookingBirthday.bookingbirthdayforkids.util.AuthenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InquiryReplyServiceImpl implements InquiryReplyService {
    @Autowired
    InquiryRepository inquiryRepository;
    @Autowired
    AccountRepository accountRepository;
    @Override
    public ResponseEntity<ResponseObj> reply(Long id, InquiryReplyRequest inquiryReplyRequest) {
        Long userId = AuthenUtil.getCurrentUserId();
        if(userId == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "Account does not exist", null));
        }
        Account account = accountRepository.findById(userId).get();
        Optional<Inquiry> inquiry = inquiryRepository.findById(id);
        if(!inquiry.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(),"Question does not exist", null));
        }
        try {
            inquiry.get().setReplyAndStatus(inquiryReplyRequest.getInquiryReply(), inquiryReplyRequest.getInquiryStatus());
            inquiry.get().setAccountReply(account);
            inquiryRepository.save(inquiry.get());
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(),"Successful",inquiry));
        }catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Invalid status for reply", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> getAll() {
        List<Inquiry> inquiryList = inquiryRepository.findAll();
        if(inquiryList.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(),"List is empty", null));
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "200", inquiryList));
    }
}

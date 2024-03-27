package com.bookingBirthday.bookingbirthdayforkids.service.impl;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.InquiryQuestionRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.model.*;
import com.bookingBirthday.bookingbirthdayforkids.repository.*;
import com.bookingBirthday.bookingbirthdayforkids.service.InquiryQuestionService;
import com.bookingBirthday.bookingbirthdayforkids.util.AuthenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class InquiryQuestionServiceImpl implements InquiryQuestionService {
    @Autowired
    InquiryRepository inquiryRepository;
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PartyBookingRepository partyBookingRepository;

    @Autowired
    PackageInVenueRepository packageInVenueRepository;
    public InquiryQuestionServiceImpl() {
        super();
    }



//    @Override
//    public ResponseEntity<ResponseObj> sendInquiryForChangePackageInVenue(Long bookingId, Long packageInVenueId) {
//        Long userId = AuthenUtil.getCurrentUserId();
//        if(userId == null){
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "400", null));
//        }
//
//        Optional<PackageInVenue> packageInVenueOptional = packageInVenueRepository.findById(packageInVenueId);
//        String packageName = packageInVenueOptional.get().getApackage().getPackageName();
//
//        Optional<PartyBooking> partyBookingOptional = partyBookingRepository.findById(bookingId);
//        PartyBooking partyBooking = partyBookingOptional.get();
//        ThemeInVenue themeInVenue = partyBooking.getThemeInVenue();
//
//        Account account = accountRepository.findById(userId).get();
//        Inquiry inquiry = new Inquiry();
//        String question = "Tại booking (id = " + bookingId + ") ngày " + partyBooking.getPartyDated().getDate() + " khung giờ từ " + partyBooking.getPartyDated().getSlotInRoom().getSlot().getTimeStart()
//                + " đến " + partyBooking.getPartyDated().getSlotInRoom().getSlot().getTimeEnd()
//                + " tại địa điểm " + themeInVenue.getVenue().getVenueName().toUpperCase()
//                + ". Tôi muốn thay đổi gói dịch vụ bữa tiệc thành " + packageName.toUpperCase();
//        inquiry.setInquiryQuestion(question);
//        inquiry.setActive(true);
//        inquiry.setStatus(InquiryStatus.PENDING);
//        inquiry.setCreateAt(LocalDateTime.now());
//        inquiry.setUpdateAt(LocalDateTime.now());
//        inquiry.setAccount(account);
//        inquiryRepository.save(inquiry);
//        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseObj(HttpStatus.CREATED.toString(), "Create Inquiry Successful", question));
//    }

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
        inquiry.setStatus(InquiryStatus.PENDING);
        inquiry.setCreateAt(LocalDateTime.now());
        inquiry.setUpdateAt(LocalDateTime.now());
        inquiry.setAccount(account);
        inquiryRepository.save(inquiry);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseObj(HttpStatus.CREATED.toString(), "Create Inquiry Successful", inquiryRequest));
    }

    @Override
    public ResponseEntity<ResponseObj> update(Long id, InquiryQuestionRequest inquiryRequest) {
        Long userId = AuthenUtil.getCurrentUserId();
        if(userId == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "400", null));
        }
        Account account = accountRepository.findById(userId).get();
        Optional<Inquiry> inquiry = inquiryRepository.findById(id);

        if (inquiry.isPresent()){
            if(!inquiry.get().getAccount().getId().equals(account.getId()))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseObj(HttpStatus.FORBIDDEN.toString(), "User not permission to update this inquiry", null));

            inquiry.get().setInquiryQuestion(inquiryRequest.getInquiryQuestion() == null ? inquiry.get().getInquiryQuestion() : inquiryRequest.getInquiryQuestion());
            inquiry.get().setUpdateAt(LocalDateTime.now());
            inquiryRepository.save(inquiry.get());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Update successful", inquiry));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Update fail", null));

    }

    @Override
    public ResponseEntity<ResponseObj> getAll() {
        Long userId = AuthenUtil.getCurrentUserId();
        if(userId == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "400", null));
        }
        List<Inquiry> inquiryList = inquiryRepository.findByAccountId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "List", inquiryList));
    }

    @Override
    public ResponseEntity<ResponseObj> delete(Long id) {
        Long userId = AuthenUtil.getCurrentUserId();
        if(userId == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "400", null));
        }
        Account account = accountRepository.findById(userId).get();
        Optional<Inquiry> inquiry = inquiryRepository.findById(id);
        if (inquiry.isPresent()){
            if(!inquiry.get().getAccount().getId().equals(account.getId()))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseObj(HttpStatus.FORBIDDEN.toString(), "User not permission to update this inquiry", null));

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

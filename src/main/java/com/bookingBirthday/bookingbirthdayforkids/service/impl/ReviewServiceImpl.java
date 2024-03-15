package com.bookingBirthday.bookingbirthdayforkids.service.impl;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.ReplyReviewRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.request.ReviewRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.model.Account;
import com.bookingBirthday.bookingbirthdayforkids.model.PartyBooking;
import com.bookingBirthday.bookingbirthdayforkids.model.Review;
import com.bookingBirthday.bookingbirthdayforkids.model.Venue;
import com.bookingBirthday.bookingbirthdayforkids.repository.AccountRepository;
import com.bookingBirthday.bookingbirthdayforkids.repository.PartyBookingRepository;
import com.bookingBirthday.bookingbirthdayforkids.repository.ReviewRepository;
import com.bookingBirthday.bookingbirthdayforkids.service.ReviewService;
import com.bookingBirthday.bookingbirthdayforkids.util.AuthenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    PartyBookingRepository partyBookingRepository;
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    AccountRepository accountRepository;
    @Override
    public ResponseEntity<ResponseObj> create(Long bookingId, ReviewRequest reviewRequest) {
        Long userId = AuthenUtil.getCurrentUserId();
        if(userId == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "400", null));
        }
        Account account = accountRepository.findById(userId).get();
        Optional<PartyBooking> partyBooking = partyBookingRepository.findById(bookingId);
        Review review = new Review();
        if(partyBooking.isPresent()){
            if(!partyBooking.get().getAccount().getId().equals(userId)){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseObj(HttpStatus.FORBIDDEN.toString(), "User not permission to review this party", null));
            }
            review.setReviewMessage(reviewRequest.getReviewMessage());
            review.setCreateAt(LocalDateTime.now());
            review.setPartyBooking(partyBooking.get());
            review.setRating(reviewRequest.getRating());
            review.setUpdateAt(LocalDateTime.now());
            review.setActive(true);
            review.setAccount(account);
            reviewRepository.save(review);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseObj(HttpStatus.CREATED.toString(), "Review Successful", review));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Review fail", null));
    }

    @Override
    public ResponseEntity<ResponseObj> reply(Long bookingId, Long id, ReplyReviewRequest replyReviewRequest) {
        Long userId = AuthenUtil.getCurrentUserId();
        if(userId == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "400", null));
        }
        Account account = accountRepository.findById(userId).get();
        Review review = reviewRepository.findById(id).get();
        Optional<PartyBooking> partyBooking = partyBookingRepository.findById(bookingId);
        if(partyBooking.isPresent()) {
            review.setAccountReply(account);
            review.setReplyMessage(replyReviewRequest.getReplyMessage());
            reviewRepository.save(review);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseObj(HttpStatus.CREATED.toString(), "Reply Successful", review));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Reply fail", null));
    }

    @Override
    public ResponseEntity<ResponseObj> getAll(Long bookingId) {
        List<Review> reviewList = reviewRepository.findAllByPartyBookingId(bookingId);
        if(reviewList.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "There are no reviews yet", null));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "List", reviewList));
    }

    @Override
    public ResponseEntity<ResponseObj> update(Long bookingId, Long id, ReviewRequest reviewRequest) {
        Long userId = AuthenUtil.getCurrentUserId();
        if(userId == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "400", null));
        }
        Account account = accountRepository.findById(userId).get();
        Review review = reviewRepository.findById(id).get();
        Optional<PartyBooking> partyBooking = partyBookingRepository.findById(bookingId);
        if(partyBooking.isPresent()) {
            if(!review.getAccount().getId().equals(account.getId()))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseObj(HttpStatus.FORBIDDEN.toString(), "User not permission to update this review", null));
            review.setReviewMessage(reviewRequest.getReviewMessage());
            review.setRating(reviewRequest.getRating() == 0 ? review.getRating() : reviewRequest.getRating());
            reviewRepository.save(review);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Review Successful", review));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Review fail", null));
    }

    @Override
    public ResponseEntity<ResponseObj> delete(Long id) {
        try {
            Optional<Review> review = reviewRepository.findById(id);
            if (review.isPresent()) {
                review.get().setActive(false);
                review.get().setDeleteAt(LocalDateTime.now());
                reviewRepository.save(review.get());
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Delete successful", null));
            } else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This review does not exist", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }
}

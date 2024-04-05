package com.bookingBirthday.bookingbirthdayforkids.service.impl;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.ReplyReviewRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.request.ReviewRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.model.*;
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
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "400", null));
        }
        Optional<PartyBooking> partyBooking = partyBookingRepository.findById(bookingId);
        if (partyBooking.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Party booking not found", null));
        }

        if (!partyBooking.get().getAccount().getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseObj(HttpStatus.FORBIDDEN.toString(), "User not permitted to review this party", null));
        }
        if (partyBooking.get().getStatus() != StatusEnum.COMPLETED) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseObj(HttpStatus.FORBIDDEN.toString(), "Party booking must be completed to review", null));
        }
        Venue venue = partyBooking.get().getSlotInRoom().getRoom().getVenue();
        if (venue == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Venue not found for this room", null));
        }
        Review review = new Review();
        review.setReviewMessage(reviewRequest.getReviewMessage());
        review.setPartyBooking(partyBooking.get());
        review.setRating(reviewRequest.getRating());
        review.setCreateAt(LocalDateTime.now());
        review.setUpdateAt(LocalDateTime.now());
        review.setActive(true);
        review.setVenue(venue);
        reviewRepository.save(review);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseObj(HttpStatus.CREATED.toString(), "Review Successful", review));
    }

    @Override
    public ResponseEntity<ResponseObj> reply(Long bookingId, Long id, ReplyReviewRequest replyReviewRequest) {
        Long userId = AuthenUtil.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "400", null));
        }
        Account account = accountRepository.findById(userId).get();
        Review review = reviewRepository.findById(id).get();
        Optional<PartyBooking> partyBooking = partyBookingRepository.findById(bookingId);
        if (partyBooking.isPresent()) {
            review.setReplyMessage(replyReviewRequest.getReplyMessage());
            reviewRepository.save(review);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseObj(HttpStatus.CREATED.toString(), "Reply Successful", review));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Reply fail", null));
    }

    @Override
    public ResponseEntity<ResponseObj> getAllReviewsByVenueIdAndRating(Long venueId, Integer rating) {
        if (rating < 0 || rating > 5) {
            return ResponseEntity.badRequest().body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Rating must be between 0 and 5", null));
        }

        List<Review> reviewList = reviewRepository.findAllByVenueIdAndRating(venueId, rating);

        if (reviewList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "There are no reviews with the specified rating", null));
        }
        for (Review review : reviewList) {
            review.setPartyBookingId(review.getPartyBooking().getId());
            review.setAccount(review.getPartyBooking().getAccount());
            review.setAccountReply(review.getVenue().getAccount());
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObj(HttpStatus.OK.toString(), "List", reviewList));
    }

    @Override
    public ResponseEntity<ResponseObj> getAllReviewsByVenueId(Long venueId) {
        List<Review> reviewList = reviewRepository.findAllByVenueId(venueId);
        if (reviewList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "There are no reviews yet", null));
        }
        for (Review review : reviewList) {
            review.setPartyBookingId(review.getPartyBooking().getId());
            review.setAccount(review.getPartyBooking().getAccount());
            review.setAccountReply(review.getVenue().getAccount());
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObj(HttpStatus.OK.toString(), "List", reviewList));
    }

    @Override
    public ResponseEntity<ResponseObj> update(Long bookingId, Long id, ReviewRequest reviewRequest) {
        Long userId = AuthenUtil.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "400", null));
        }
        Account account = accountRepository.findById(userId).get();
        Review review = reviewRepository.findById(id).get();
        Optional<PartyBooking> partyBooking = partyBookingRepository.findById(bookingId);
        if (partyBooking.isPresent()) {
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

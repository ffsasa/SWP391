package com.bookingBirthday.bookingbirthdayforkids.service;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.ReplyReviewRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.request.ReviewRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import org.springframework.http.ResponseEntity;

public interface ReviewService {
    public ResponseEntity<ResponseObj> create(Long bookingId, ReviewRequest reviewRequest);

    public ResponseEntity<ResponseObj> reply(Long bookingId, Long id, ReplyReviewRequest replyReviewRequest);

    ResponseEntity<ResponseObj> getAllReviewsByVenueIdAndRating(Long venueId, Integer rating);

    ResponseEntity<ResponseObj> getAllReviewsByVenueId(Long venueId);

    ResponseEntity<ResponseObj> getAllReviewsForHost();

    ResponseEntity<ResponseObj> getAllReviewsForHostByRating(Integer rating);

    public ResponseEntity<ResponseObj> update(Long bookingId, Long id, ReviewRequest reviewRequest);

    public ResponseEntity<ResponseObj> delete(Long id);

}

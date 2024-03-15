package com.bookingBirthday.bookingbirthdayforkids.service;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.ReplyReviewRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.request.ReviewRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import org.springframework.http.ResponseEntity;

public interface ReviewService {
    public ResponseEntity<ResponseObj> create(Long bookingId, ReviewRequest reviewRequest);
    public ResponseEntity<ResponseObj> getAll(Long bookingId);

    public ResponseEntity<ResponseObj> reply(Long bookingId, Long id, ReplyReviewRequest replyReviewRequest);

    public ResponseEntity<ResponseObj> update(Long bookingId, Long id, ReviewRequest reviewRequest);

    public ResponseEntity<ResponseObj> delete(Long id);

}

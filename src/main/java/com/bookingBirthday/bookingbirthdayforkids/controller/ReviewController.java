package com.bookingBirthday.bookingbirthdayforkids.controller;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.ReplyReviewRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.request.ReviewRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/review")
public class ReviewController {
    @Autowired
    ReviewService reviewService;
    @PostMapping("/create/{bookingId}")
    public ResponseEntity<ResponseObj> create(@PathVariable Long bookingId, @Valid @RequestBody ReviewRequest reviewRequest){
        return reviewService.create(bookingId, reviewRequest);
    }
    @PostMapping("/reply/{bookingId}/{id}")
    public ResponseEntity<ResponseObj> reply(@PathVariable Long bookingId,@PathVariable Long id, @Valid @RequestBody ReplyReviewRequest replyReviewRequest){
        return reviewService.reply(bookingId, id, replyReviewRequest);
    }

    @GetMapping("/get-all/{id}")
    public ResponseEntity<ResponseObj> getAll(@PathVariable Long id){
        return reviewService.getAll(id);
    }

    @PutMapping("/update-review/{bookingId}/{id}")
    public ResponseEntity<ResponseObj> update(@PathVariable Long bookingId,@PathVariable Long id, @Valid @RequestBody ReviewRequest reviewRequest){
        return reviewService.update(bookingId, id, reviewRequest);
    }

    @DeleteMapping("/delete-review/{id}")
    public ResponseEntity<ResponseObj> delete(@PathVariable Long id){
        return reviewService.delete(id);
    }

}

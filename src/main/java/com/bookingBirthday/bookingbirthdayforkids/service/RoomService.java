package com.bookingBirthday.bookingbirthdayforkids.service;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.RoomRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

public interface RoomService {
    public ResponseEntity<ResponseObj> getAll();

    public ResponseEntity<ResponseObj> getById(Long id);

    public ResponseEntity<ResponseObj> getSlotInRoomById(Long roomId);

    public ResponseEntity<ResponseObj> checkSlotInRoom(LocalDateTime date);

    public ResponseEntity<ResponseObj> create(MultipartFile fileImg, String roomName, Long venueId, int capacity, float parsedPricing);

    public ResponseEntity<ResponseObj> update(Long id, RoomRequest roomRequest);

    public ResponseEntity<ResponseObj> delete(Long id);
}

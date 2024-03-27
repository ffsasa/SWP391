package com.bookingBirthday.bookingbirthdayforkids.service;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.SlotInRoomRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import org.springframework.http.ResponseEntity;

public interface SlotInRoomService {
    ResponseEntity<ResponseObj> create(SlotInRoomRequest slotInRoomRequest);
    ResponseEntity<ResponseObj> disableSlotInRoom(Long id);
    ResponseEntity<ResponseObj> activeSlotInRoom(Long id);

}

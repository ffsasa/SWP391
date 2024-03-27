package com.bookingBirthday.bookingbirthdayforkids.service;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.SlotInVenueRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import org.springframework.http.ResponseEntity;

public interface SlotInRoomService {
    ResponseEntity<ResponseObj> create(SlotInVenueRequest slotInVenueRequest);
    ResponseEntity<ResponseObj> disableSlotInVenue(Long id);
    ResponseEntity<ResponseObj> activeSlotInVenue(Long id);

}

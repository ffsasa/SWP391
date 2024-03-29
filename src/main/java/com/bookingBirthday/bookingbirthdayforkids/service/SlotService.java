package com.bookingBirthday.bookingbirthdayforkids.service;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.SlotRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface SlotService {
    public ResponseEntity<ResponseObj> getAll();

    public ResponseEntity<ResponseObj> getAllForHost();


    public ResponseEntity<ResponseObj> getById(Long id);

    public ResponseEntity<ResponseObj> getById_ForCustomer(Long id);

    public ResponseEntity<ResponseObj> create(SlotRequest slotRequest);

    public ResponseEntity<ResponseObj> update(Long id, SlotRequest slotRequest);

    public ResponseEntity<ResponseObj> delete(Long id);
    ResponseEntity<ResponseObj> addSlotInRoomByRoomId(Long roomId, List<Long> slotId);
}

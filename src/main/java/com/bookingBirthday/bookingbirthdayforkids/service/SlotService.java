package com.bookingBirthday.bookingbirthdayforkids.service;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.SlotRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface SlotService {
    public ResponseEntity<ResponseObj> getAllSlotForCustomer();

    public ResponseEntity<ResponseObj> getAllSlotForHost();


    public ResponseEntity<ResponseObj> getByIdForHost(Long id);

    public ResponseEntity<ResponseObj> getByIdForCustomer(Long id);

    public ResponseEntity<ResponseObj> create(SlotRequest slotRequest);

    public ResponseEntity<ResponseObj> update(Long id, SlotRequest slotRequest);

    public ResponseEntity<ResponseObj> delete(Long id);
    ResponseEntity<ResponseObj> addSlotInRoomByRoomId(Long roomId, List<Long> slotId);
}

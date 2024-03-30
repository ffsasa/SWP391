package com.bookingBirthday.bookingbirthdayforkids.service;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.RoomRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface RoomService {
    public ResponseEntity<ResponseObj> getAllRoomInVenueByCustomer(Long venueId);

    public ResponseEntity<ResponseObj> getAllRoomInVenueByHost();

    public ResponseEntity<ResponseObj> getRoomInVenueByIdForCustomer(Long roomId, Long venueId);

    public ResponseEntity<ResponseObj> getRoomInVenueByIdForHost(Long roomId);

    public ResponseEntity<ResponseObj> getSlotInRoomByIdForCustomer(Long roomId, Long venueId);

    public ResponseEntity<ResponseObj> getSlotInRoomByIdForHost(Long roomId);

    public ResponseEntity<ResponseObj> getSlotNotAddInRoomByIdForHost(Long roomId);

    public ResponseEntity<ResponseObj> checkSlotInRoomForCustomer(LocalDate date, Long venueId);

    public ResponseEntity<ResponseObj> enable(Long roomId);


    public ResponseEntity<ResponseObj> checkSlotInRoomForHost(LocalDate date);

    public ResponseEntity<ResponseObj> create(MultipartFile fileImg, String roomName, int capacity, float parsedPricing);

    public ResponseEntity<ResponseObj> update(Long id, MultipartFile fileImg, String roomName,int capacity, float parsedPricing);

    public ResponseEntity<ResponseObj> delete(Long roomId);
}

package com.bookingBirthday.bookingbirthdayforkids.service.impl;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.SlotInRoomRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.model.*;
import com.bookingBirthday.bookingbirthdayforkids.repository.*;
import com.bookingBirthday.bookingbirthdayforkids.service.SlotInRoomService;
import com.bookingBirthday.bookingbirthdayforkids.util.AuthenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SlotInRoomServiceImpl implements SlotInRoomService {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    SlotRepository slotRepository;
    @Autowired
    VenueRepository venueRepository;
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    SlotInRoomRepository slotInRoomRepository;

    @Override
    public ResponseEntity<ResponseObj> create(SlotInRoomRequest slotInRoomRequest, Long venueId) {
        Long useId = AuthenUtil.getCurrentUserId();
        Optional<Account> account = accountRepository.findById(useId);
        Optional<Slot> slot = slotRepository.findById(slotInRoomRequest.getSlot_id());
        Optional<Room> room = roomRepository.findById(slotInRoomRequest.getRoom_id());
        Optional<Venue> venue = venueRepository.findById(venueId);
        if (!venue.get().getAccount().getId().equals(account.get().getId())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "You are not permission", null));
        }
        if (!slot.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Slot does not exist", null));
        }
        if (!room.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Room does not exist", null));
        }
        if (!slot.get().getAccount().getId().equals(account.get().getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Slot not exist in this venue", null));
        }
        if (!room.get().getVenue().getId().equals(venue.get().getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Room not exist in this venue", null));
        }
        if (slotInRoomRepository.existsBySlotIdAndRoomId(slotInRoomRequest.getSlot_id(), slotInRoomRequest.getRoom_id())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Slot in room existed", null));
        }
        SlotInRoom slotInRoom = new SlotInRoom();
        slotInRoom.setSlot(slot.get());
        slotInRoom.setRoom(room.get());
        slotInRoom.setCreateAt(LocalDateTime.now());
        slotInRoom.setActive(true);
        slotInRoom.setUpdateAt(LocalDateTime.now());
        slotInRoomRepository.save(slotInRoom);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseObj(HttpStatus.CREATED.toString(), "Create successful", slotInRoom));
    }

    @Override
    public ResponseEntity<ResponseObj> disableSlotInRoom(Long id) {
        Long userId = AuthenUtil.getCurrentUserId();
        Optional<Account> account = accountRepository.findById(userId);
        List<Slot> slotList = account.get().getSlotList();
        for(Slot slot : slotList) {
            List<SlotInRoom> slotInRoomList = slot.getSlotInRoom();
            for (SlotInRoom slotInRoom : slotInRoomList) {
                if (slotInRoom.getId().equals(id)) {
                    slotInRoom.setActive(false);
                    slotInRoomRepository.save(slotInRoom);
                    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Disable successful", slotInRoom));
                }
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Slot in venue does not exist", null));
    }

    @Override
    public ResponseEntity<ResponseObj> activeSlotInRoom(Long id) {
        Long userId = AuthenUtil.getCurrentUserId();
        Optional<Account> account = accountRepository.findById(userId);
        List<Slot> slotList = account.get().getSlotList();
        for(Slot slot : slotList){
            List<SlotInRoom> slotInRoomList = slot.getSlotInRoom();
            for(SlotInRoom slotInRoom : slotInRoomList){
                if(slotInRoom.getId().equals(id)){
                    slotInRoom.setActive(true);
                    slotInRoom.setUpdateAt(LocalDateTime.now());
                    slotInRoomRepository.save(slotInRoom);
                    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Active successful", slotInRoom));
                }
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Slot in venue does not exist", null));
    }
}

package com.bookingBirthday.bookingbirthdayforkids.service.impl;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.SlotInRoomRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.model.Room;
import com.bookingBirthday.bookingbirthdayforkids.model.Slot;
import com.bookingBirthday.bookingbirthdayforkids.model.SlotInRoom;
import com.bookingBirthday.bookingbirthdayforkids.model.Venue;
import com.bookingBirthday.bookingbirthdayforkids.repository.RoomRepository;
import com.bookingBirthday.bookingbirthdayforkids.repository.SlotInRoomRepository;
import com.bookingBirthday.bookingbirthdayforkids.repository.SlotRepository;
import com.bookingBirthday.bookingbirthdayforkids.repository.VenueRepository;
import com.bookingBirthday.bookingbirthdayforkids.service.SlotInRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SlotInRoomServiceImpl implements SlotInRoomService {
    @Autowired
    SlotRepository slotRepository;
    @Autowired
    VenueRepository venueRepository;
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    SlotInRoomRepository slotInRoomRepository;
    @Override
    public ResponseEntity<ResponseObj> create(SlotInRoomRequest slotInRoomRequest) {
        Optional<Slot> slot = slotRepository.findById(slotInRoomRequest.getSlot_id());
        Optional<Room> room = roomRepository.findById(slotInRoomRequest.getRoom_id());
        if (!slot.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Slot does not exist", null));
        }
        if(!room.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Room does not exist", null));
        }
        if(slotInRoomRepository.existsBySlotIdAndRoomId(slotInRoomRequest.getSlot_id(), slotInRoomRequest.getRoom_id())){
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
        Optional<SlotInRoom> slotInRoom = slotInRoomRepository.findById(id);
        if(slotInRoom.isPresent()){
            slotInRoom.get().setActive(false);
            slotInRoomRepository.save(slotInRoom.get());
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Disable successful", slotInRoom));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Slot in venue does not exist", null));
    }

    @Override
    public ResponseEntity<ResponseObj> activeSlotInRoom(Long id) {
        Optional<SlotInRoom> slotInRoom = slotInRoomRepository.findById(id);
        if(slotInRoom.isPresent()){
            if (!slotInRoom.get().getRoom().isActive())
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Room is not active. You can not enable slotInVenue", null));

            slotInRoom.get().setActive(true);
            slotInRoom.get().setUpdateAt(LocalDateTime.now());
            slotInRoomRepository.save(slotInRoom.get());
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Active successful", slotInRoom));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Slot in venue does not exist", null));
    }
}

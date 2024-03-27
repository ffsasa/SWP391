package com.bookingBirthday.bookingbirthdayforkids.service.impl;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.RoomRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.model.PartyDated;
import com.bookingBirthday.bookingbirthdayforkids.model.Room;
import com.bookingBirthday.bookingbirthdayforkids.model.SlotInRoom;
import com.bookingBirthday.bookingbirthdayforkids.model.Venue;
import com.bookingBirthday.bookingbirthdayforkids.repository.PartyDatedRepository;
import com.bookingBirthday.bookingbirthdayforkids.repository.RoomRepository;
import com.bookingBirthday.bookingbirthdayforkids.repository.VenueRepository;
import com.bookingBirthday.bookingbirthdayforkids.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    VenueRepository venueRepository;

    @Autowired
    PartyDatedRepository partyDatedRepository;
    @Override
    public ResponseEntity<ResponseObj> getAll() {
        List<Room> roomList = roomRepository.findAllByIsActiveIsTrue();
        if (roomList.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "List is empty", null));

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "OK", roomList));
    }

    @Override
    public ResponseEntity<ResponseObj> getById(Long id) {
        try {
            Optional<Room> room= roomRepository.findById(id);
            if(room.isPresent())
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), null, room.get()));
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Room does not exist", null));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> create(RoomRequest roomRequest) {
        try {
            Optional<Venue> venue = venueRepository.findById(roomRequest.getVenueId());
            if (venue.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Venue not found", null));
            }
            Room room = new Room();
            room.setVenue(venue.get());
            room.setRoomName(roomRequest.getRoomName());
            room.setRoomImgUrl(room.getRoomImgUrl());
            room.setCapacity(roomRequest.getCapacity());
            room.setPricing(roomRequest.getPricing());
            room.setActive(true);
            room.setCreateAt(LocalDateTime.now());
            room.setUpdateAt(LocalDateTime.now());
            roomRepository.save(room);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseObj(HttpStatus.CREATED.toString(), "Room created successfully", room));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }
    @Override
    public ResponseEntity<ResponseObj> update(Long id, RoomRequest roomRequest) {
        try {
            Optional<Room> roomOptional = roomRepository.findById(id);
            if (roomOptional.isPresent()) {
                Room room = roomOptional.get();
                room.setRoomName(roomRequest.getRoomName());
                room.setRoomImgUrl(roomRequest.getRoomImgUrl());
                room.setCapacity(roomRequest.getCapacity());
                room.setPricing(roomRequest.getPricing());
                room.setUpdateAt(LocalDateTime.now());
                roomRepository.save(room);
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Room updated successfully", room));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Room not found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> getSlotInRoomById(Long roomId) {
        try {
            Optional<Room> room = roomRepository.findById(roomId);
            if (room.isPresent()) {
                List<SlotInRoom> slotInRoomList = room.get().getSlotInRoomList();
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", slotInRoomList));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This room does not exist", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> delete(Long id) {
        try {
            Optional<Room> roomOptional = roomRepository.findById(id);
            if (roomOptional.isPresent()) {
                Room room = roomOptional.get();
                room.setActive(false);
                room.setUpdateAt(LocalDateTime.now());
                roomRepository.save(room);
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Room deleted successfully", null));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Room not found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }

    }

    //thêm
    @Override
    public ResponseEntity<ResponseObj> checkSlotInRoom(LocalDateTime date) {
        try {
            LocalDateTime currentDateTime = LocalDateTime.now();;
            LocalDateTime chooseDateTime = date.withHour(0).withMinute(0).withSecond(0);

            if (currentDateTime.isAfter(chooseDateTime.plusHours(6)) ) {
                List<SlotInRoom> slotInRoomList = new ArrayList<>();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "The date has expired", slotInRoomList));
            }
            List<Room> roomList = roomRepository.findAllByIsActiveIsTrue();
            if (roomList.isEmpty())
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "List is empty", null));

            List<PartyDated> partyDatedList = partyDatedRepository.findByDateAndIsActiveIsTrue(date.toLocalDate());

            for (Room room : roomList) {
                List<SlotInRoom> slotInRoomList = room.getSlotInRoomList();
                List<SlotInRoom> slotInRoomListValidate = new ArrayList<>();
                for (SlotInRoom slotInRoom : slotInRoomList) {
                    if (slotInRoom.isActive()) {
                        slotInRoomListValidate.add(slotInRoom);
                    }
                }
                for (SlotInRoom slotInRoom : slotInRoomListValidate) {
                    for (PartyDated partyDated : partyDatedList) {
                        if (partyDated.getSlotInRoom().equals(slotInRoom)) {
                            slotInRoom.setStatus(true);
                        }
                    }
                }
                room.setSlotInRoomList(slotInRoomListValidate);
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", roomList));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

//    @Override
//    public ResponseEntity<ResponseObj> checkSlotInVenueForHost(LocalDate date) {
//        try {
//            List<Venue> venueList = venueRepository.findAll();
//            if (venueList.isEmpty())
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "List venue is empty", null));
//
//            List<PartyDated> partyDatedList = partyDatedRepository.findByDateAndIsActiveIsTrue(date);
//
//            for (Venue venue : venueList) {
//                List<SlotInRoom> slotInRoomList = venue.getSlotInRoomList();
//                for (SlotInRoom slotInRoom : slotInRoomList) {
//
//                    for (PartyDated partyDated : partyDatedList) {
//                        if (partyDated.getSlotInRoom().equals(slotInRoom)) {
//                            slotInRoom.setPartyDated(partyDated);
//                            slotInRoom.setStatus(true);
//                        }
//                    }
//                }
//            }
//            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", venueList));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
//        }
//    }
}

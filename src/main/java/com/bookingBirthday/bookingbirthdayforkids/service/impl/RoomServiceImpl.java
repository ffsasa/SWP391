package com.bookingBirthday.bookingbirthdayforkids.service.impl;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.RoomRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.model.*;
import com.bookingBirthday.bookingbirthdayforkids.model.Package;
import com.bookingBirthday.bookingbirthdayforkids.repository.PartyDatedRepository;
import com.bookingBirthday.bookingbirthdayforkids.repository.RoomRepository;
import com.bookingBirthday.bookingbirthdayforkids.repository.SlotRepository;
import com.bookingBirthday.bookingbirthdayforkids.repository.VenueRepository;
import com.bookingBirthday.bookingbirthdayforkids.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
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
    SlotRepository slotRepository;
    @Autowired
    PartyDatedRepository partyDatedRepository;
    @Autowired
    FirebaseService firebaseService;
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
    public ResponseEntity<ResponseObj> create(MultipartFile fileImg, String roomName, Long venueId, int capacity, float parsedPricing) {
        Optional<Venue> venue = venueRepository.findById(venueId);
        if(!venue.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Venue does not exist", null));
        }
        if(roomRepository.existsByRoomNameAndVenue(roomName, venue.get())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(),"This room name has already exist in this venue", null));
        }

        Room room = new Room();
        try {
            if (fileImg != null) {
                String img = firebaseService.uploadImage(fileImg);
                room.setRoomName(roomName);
                room.setVenue(venue.get());
                room.setCapacity(capacity);
                room.setRoomImgUrl(img);
                room.setPricing(parsedPricing);
                room.setActive(true);
                room.setCreateAt(LocalDateTime.now());
                room.setUpdateAt(LocalDateTime.now());
                roomRepository.save(room);
            }

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Image is invalid", null));

        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Create successful", room));
    }
    @Override
    public ResponseEntity<ResponseObj> update(Long id, MultipartFile fileImg, String roomName, int capacity, float parsedPricing) {
        try {
            Optional<Room> room = roomRepository.findById(id);
            if (room.isPresent()) {
                room.get().setRoomImgUrl(fileImg == null ? room.get().getRoomImgUrl() : firebaseService.uploadImage(fileImg));
                room.get().setRoomName(roomName == null ? room.get().getRoomName() : roomName);
                room.get().setCapacity(capacity == 0 ? room.get().getCapacity() : capacity);
                room.get().setPricing(parsedPricing == 0 ? room.get().getPricing() : parsedPricing);
                room.get().setUpdateAt(LocalDateTime.now());
                roomRepository.save(room.get());
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Room updated successfully", room));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Room not found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    public ResponseEntity<ResponseObj> getSlotNotAddInRoomById(Long roomId){
        try {
            Optional<Room> room = roomRepository.findById(roomId);
            if (room.isPresent()) {
                List<SlotInRoom> slotInRoomList = room.get().getSlotInRoomList();
                List<Slot> slotAddedList = new ArrayList<>();
                for (SlotInRoom slotInRoom : slotInRoomList) {
                    slotAddedList.add(slotInRoom.getSlot());
                }
                List<Slot> slotList = slotRepository.findAll();
                List<Slot> slotNotAddList = new ArrayList<>();
                for (Slot slot : slotList) {
                    if (!slotAddedList.contains(slot)) {
                        slotNotAddList.add(slot);
                    }
                }
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", slotNotAddList));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "This theme does not exist", null));
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

    @Override
    public ResponseEntity<ResponseObj> checkSlotInRoomForHost(LocalDate date) {
        try {
            List<Room> roomList = roomRepository.findAll();
            if (roomList.isEmpty())
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "List room is empty", null));

            List<PartyDated> partyDatedList = partyDatedRepository.findByDateAndIsActiveIsTrue(date);

            for (Room room : roomList) {
                List<SlotInRoom> slotInRoomList = room.getSlotInRoomList();
                for (SlotInRoom slotInRoom : slotInRoomList) {

                    for (PartyDated partyDated : partyDatedList) {
                        if (partyDated.getSlotInRoom().equals(slotInRoom)) {
                            slotInRoom.setPartyDated(partyDated);
                            slotInRoom.setStatus(true);
                        }
                    }
                }
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", roomList));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }
}

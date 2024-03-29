package com.bookingBirthday.bookingbirthdayforkids.service.impl;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.RoomRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.model.*;
import com.bookingBirthday.bookingbirthdayforkids.model.Package;
import com.bookingBirthday.bookingbirthdayforkids.repository.*;
import com.bookingBirthday.bookingbirthdayforkids.service.RoomService;
import com.bookingBirthday.bookingbirthdayforkids.util.AuthenUtil;
import org.checkerframework.checker.nullness.qual.AssertNonNullIfNonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
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
    AccountRepository accountRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    VenueRepository venueRepository;

    @Autowired
    SlotRepository slotRepository;
    @Autowired
    FirebaseService firebaseService;

    @Autowired
    PartyBookingRepository partyBookingRepository;

    @Override
    public ResponseEntity<ResponseObj> getAllRoomInVenueByCustomer(Long venueId) {
        Optional<Venue> venue = venueRepository.findById(venueId);
        List<Room> roomList = venue.get().getRoomList();
        List<Room> roomListCustomer = new ArrayList<>();
        if (!venue.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "Venue does not exist", null));
        }
        for(Room room : roomList){
            if(room.isActive()){
                roomListCustomer.add(room);
            }
        }
        if (roomListCustomer.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "List is empty", null));

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Ok", roomListCustomer));

    }

    @Override
    public ResponseEntity<ResponseObj> getAllRoomInVenueByHost(Long venueId) {
        Long userId = AuthenUtil.getCurrentUserId();
        Optional<Account> account = accountRepository.findById(userId);
        Optional<Venue> venue = venueRepository.findById(venueId);
        if (!venue.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "Venue does not exist", null));
        }
        if (!account.get().getId().equals(venue.get().getAccount().getId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "You are not permission", null));
        }
        List<Room> roomList = venue.get().getRoomList();
        if (roomList.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "List is empty", null));

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Ok", roomList));

    }

    @Override
    public ResponseEntity<ResponseObj> getRoomInVenueByIdForCustomer(Long roomId, Long venueId) {
        try {
            Optional<Venue> venue = venueRepository.findById(venueId);
            List<Room> roomList = venue.get().getRoomList();
            if (!venue.isPresent()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "Venue does not exist", null));
            }
            for(Room room : roomList){
                if(room.getId().equals(roomId)){
                    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), null, room));
                }
            }
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Room does not exist", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> getRoomInVenueByIdForHost(Long roomId, Long venueId) {
        try {
            Long userId = AuthenUtil.getCurrentUserId();
            Optional<Account> account = accountRepository.findById(userId);
            Optional<Venue> venue = venueRepository.findById(venueId);
            if (!venue.isPresent()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "Venue does not exist", null));
            }
            if (!account.get().getId().equals(venue.get().getAccount().getId())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "You are not permission", null));
            }
            List<Room> roomList = venue.get().getRoomList();
            for(Room room : roomList){
                if(room.getId().equals(roomId)){
                    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Ok", room));
                }
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Room does not exist", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> getSlotNotAddInRoomByIdForHost(Long roomId, Long venueId) {
        try {
            Long userId = AuthenUtil.getCurrentUserId();
            Optional<Account> account = accountRepository.findById(userId);
            Optional<Venue> venue = venueRepository.findById(venueId);
            if (!venue.isPresent()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "Venue does not exist", null));
            }
            if (!account.get().getId().equals(venue.get().getAccount().getId())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "You are not permission", null));
            }
            List<Room> roomList = venue.get().getRoomList();
            for (Room room : roomList) {
                if (room.getId().equals(roomId)) {
                    List<SlotInRoom> slotInRoomList = room.getSlotInRoomList();
                    List<Slot> slotAddedList = new ArrayList<>();
                    for (SlotInRoom slotInRoom : slotInRoomList) {
                        slotAddedList.add(slotInRoom.getSlot());
                    }
                    List<Slot> slotList = account.get().getSlotList();
                    List<Slot> slotNotAddList = new ArrayList<>();
                    for (Slot slot : slotList) {
                        if (!slotAddedList.contains(slot)) {
                            slotNotAddList.add(slot);
                        }
                    }
                    return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", slotNotAddList));
                }
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This room does not exist", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }


    @Override
    public ResponseEntity<ResponseObj> getSlotInRoomByIdForHost(Long roomId, Long venueId) {
        try {
            Long userId = AuthenUtil.getCurrentUserId();
            Optional<Account> account = accountRepository.findById(userId);
            Venue venue = account.get().getVenue();
            List<Room> roomList = venue.getRoomList();
            for(Room room : roomList){
                if(room.getId().equals(roomId)){
                    List<SlotInRoom> slotInRoomList = room.getSlotInRoomList();
                    return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", slotInRoomList));
                }
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This room does not exist", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> getSlotInRoomByIdForCustomer(Long roomId, Long venueId) {
        try {
            Optional<Venue> venue = venueRepository.findById(venueId);
            List<Room> roomList = venue.get().getRoomList();
            if (!venue.isPresent()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "Venue does not exist", null));
            }
            for(Room room : roomList){
                if(room.getId().equals(roomId)){
                    List<SlotInRoom> slotInRoomList = room.getSlotInRoomList();
                    return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", slotInRoomList));
                }
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This room does not exist", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }


    @Override
    public ResponseEntity<ResponseObj> create(MultipartFile fileImg, String roomName, Long venueId, int capacity, float parsedPricing) {
        Long userId = AuthenUtil.getCurrentUserId();
        Optional<Account> account = accountRepository.findById(userId);
        Optional<Venue> venue = venueRepository.findById(venueId);
        if (!venue.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "Venue does not exist", null));
        }
        if (!account.get().getId().equals(venue.get().getAccount().getId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "You are not permission", null));
        }
        if (roomRepository.existsByRoomNameAndVenue(roomName, venue.get())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "This room name has already exist in this venue", null));
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

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Image is invalid", null));

        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Create successful", room));
    }

    @Override
    public ResponseEntity<ResponseObj> update(Long id, Long venueId, MultipartFile fileImg, String roomName, int capacity, float parsedPricing) {
        try {
            Long userId = AuthenUtil.getCurrentUserId();
            Optional<Account> account = accountRepository.findById(userId);
            Optional<Venue> venue = venueRepository.findById(venueId);
            if (!venue.isPresent()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "Venue does not exist", null));
            }
            if (!account.get().getId().equals(venue.get().getAccount().getId())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "You are not permission", null));
            }
            List<Room> roomList = venue.get().getRoomList();
            for (Room room : roomList) {
                if (room.getId().equals(id)) {
                    room.setRoomImgUrl(fileImg == null ? room.getRoomImgUrl() : firebaseService.uploadImage(fileImg));
                    room.setRoomName(roomName == null ? room.getRoomName() : roomName);
                    room.setCapacity(capacity == 0 ? room.getCapacity() : capacity);
                    room.setPricing(parsedPricing == 0 ? room.getPricing() : parsedPricing);
                    room.setUpdateAt(LocalDateTime.now());
                    room.setVenue(venue.get());
                    roomRepository.save(room);
                    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Room updated successfully", room));
                }
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Room not found", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> delete(Long roomId, Long venueId) {
        try {
            Long userId = AuthenUtil.getCurrentUserId();
            Optional<Account> account = accountRepository.findById(userId);
            Optional<Venue> venue = venueRepository.findById(venueId);
            if (!venue.isPresent()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "Venue does not exist", null));
            }
            if (!account.get().getId().equals(venue.get().getAccount().getId())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "You are not permission", null));
            }
            List<Room> roomList = venue.get().getRoomList();
            for (Room room : roomList) {
                if (room.getId().equals(roomId)) {
                    room.setActive(false);
                    room.setDeleteAt(LocalDateTime.now());
                    room.setVenue(venue.get());
                    roomRepository.save(room);

                }
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Room not found", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }

    }



    //thêm
    @Override
    public ResponseEntity<ResponseObj> checkSlotInRoomForCustomer(LocalDateTime date, Long venueId) {
        try {
            LocalDateTime currentDateTime = LocalDateTime.now();
            ;
            LocalDateTime chooseDateTime = date.withHour(0).withMinute(0).withSecond(0);

            if (currentDateTime.isAfter(chooseDateTime.plusHours(6))) {
                List<SlotInRoom> slotInRoomList = new ArrayList<>();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "The date has expired", slotInRoomList));
            }
            Optional<Venue> venue = venueRepository.findById(venueId);
            List<Room> roomList = venue.get().getRoomList();
            List<Room> roomListCustomer = new ArrayList<>();
            for(Room room : roomList){
                if(room.isActive()){
                    roomListCustomer.add(room);
                }
            }
            if (roomListCustomer.isEmpty())
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "List is empty", null));
            List<PartyBooking> partyBookingList = partyBookingRepository.findAllByDateAndIsActiveIsTrue(date.toLocalDate());

            for(Room room : roomListCustomer){
                List<SlotInRoom> slotInRoomList = room.getSlotInRoomList();
                List<SlotInRoom> slotInRoomValidate = new ArrayList<>();
                for(SlotInRoom slotInRoom : slotInRoomList){
                    if(slotInRoom.isActive()){
                        slotInRoomValidate.add(slotInRoom);
                    }
                }

                for(SlotInRoom slotInRoom : slotInRoomValidate){
                    for(PartyBooking partyBooking : partyBookingList){
                        if(partyBooking.getSlotInRoom().equals(slotInRoom)){
                            slotInRoom.setStatus(true);
                        }
                    }
                }
                room.setSlotInRoomList(slotInRoomValidate);
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", roomList));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> checkSlotInRoomForHost(LocalDate date) {
        try {
            Long userId = AuthenUtil.getCurrentUserId();
            Optional<Account> account =accountRepository.findById(userId);
            Venue venue = account.get().getVenue();
            List<Room> roomList = venue.getRoomList();
            if (roomList.isEmpty())
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "List room is empty", null));

            List<PartyBooking> partyBookingList = partyBookingRepository.findAllByDateAndIsActiveIsTrue(date);

            for (Room room : roomList) {
                List<SlotInRoom> slotInRoomList = room.getSlotInRoomList();
                for (SlotInRoom slotInRoom : slotInRoomList) {

                    for (PartyBooking partyBooking : partyBookingList) {
                        if (partyBooking.getSlotInRoom().equals(slotInRoom)) {
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

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
        for (Room room : roomList) {
            if (room.isActive()) {
                roomListCustomer.add(room);
            }
        }
        if (roomListCustomer.isEmpty())
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "List is empty", roomListCustomer));

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Ok", roomListCustomer));

    }

    @Override
    public ResponseEntity<ResponseObj> getAllRoomInVenueByHost() {
        Long userId = AuthenUtil.getCurrentUserId();
        Optional<Account> account = accountRepository.findById(userId);
        Venue venue = account.get().getVenue();
        List<Room> roomList = venue.getRoomList();
        if (roomList.isEmpty())
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "List is empty", roomList));

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Ok", roomList));

    }

    @Override
    public ResponseEntity<ResponseObj> getAllRoomInVenueIsTrueByHost() {
        Long userId = AuthenUtil.getCurrentUserId();
        Optional<Account> account = accountRepository.findById(userId);
        Venue venue = account.get().getVenue();
        List<Room> roomList = venue.getRoomList();
        List<Room> roomListIsTrue = new ArrayList<>();
        if (roomList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "List is empty", roomList));
        }
        for (Room room : roomList) {
            if (room.isActive()) {
                roomListIsTrue.add(room);
            }
        }
        if (roomListIsTrue.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "List is empty", roomListIsTrue));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Ok", roomListIsTrue));
    }

    @Override
    public ResponseEntity<ResponseObj> getAllRoomInVenueIsFalseByHost() {
        Long userId = AuthenUtil.getCurrentUserId();
        Optional<Account> account = accountRepository.findById(userId);
        Venue venue = account.get().getVenue();
        List<Room> roomList = venue.getRoomList();
        List<Room> roomListIsFalse = new ArrayList<>();
        if (roomList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "List is empty", roomList));
        }
        for (Room room : roomList) {
            if (!room.isActive()) {
                roomListIsFalse.add(room);
            }
        }
        if (roomListIsFalse.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "List is empty", roomListIsFalse));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Ok", roomListIsFalse));
    }

    @Override
    public ResponseEntity<ResponseObj> getRoomInVenueByIdForCustomer(Long roomId, Long venueId) {
        try {
            Optional<Venue> venue = venueRepository.findById(venueId);
            List<Room> roomList = venue.get().getRoomList();
            if (!venue.isPresent()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "Venue does not exist", null));
            }
            for (Room room : roomList) {
                if (room.getId().equals(roomId)) {
                    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), null, room));
                }
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Room does not exist", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> getRoomInVenueByIdForHost(Long roomId) {
        try {
            Long userId = AuthenUtil.getCurrentUserId();
            Optional<Account> account = accountRepository.findById(userId);
            Venue venue = account.get().getVenue();
            List<Room> roomList = venue.getRoomList();
            for (Room room : roomList) {
                if (room.getId().equals(roomId)) {
                    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Ok", room));
                }
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Room does not exist", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> getSlotNotAddInRoomByIdForHost(Long roomId) {
        try {
            Long userId = AuthenUtil.getCurrentUserId();
            Optional<Account> account = accountRepository.findById(userId);
            Venue venue = account.get().getVenue();
            List<Room> roomList = venue.getRoomList();
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
    public ResponseEntity<ResponseObj> getSlotInRoomByIdForHost(Long roomId) {
        try {
            Long userId = AuthenUtil.getCurrentUserId();
            Optional<Account> account = accountRepository.findById(userId);
            Venue venue = account.get().getVenue();
            List<Room> roomList = venue.getRoomList();
            for (Room room : roomList) {
                if (room.getId().equals(roomId)) {
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
            for (Room room : roomList) {
                if (room.getId().equals(roomId)) {
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
    public ResponseEntity<ResponseObj> create(MultipartFile fileImg, String roomName, int capacity, float parsedPricing) {
        Long userId = AuthenUtil.getCurrentUserId();
        Optional<Account> account = accountRepository.findById(userId);
        Venue venue = account.get().getVenue();
        if (roomRepository.existsByRoomNameAndVenue(roomName, venue)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "This room name has already exist in this venue", null));
        }

        Room room = new Room();
        try {
            if (fileImg != null) {
                String img = firebaseService.uploadImage(fileImg);
                room.setRoomName(roomName);
                room.setVenue(venue);
                room.setCapacity(capacity);
                room.setRoomImgUrl(img);
                room.setPricing(parsedPricing);
                room.setActive(false);
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
    public ResponseEntity<ResponseObj> update(Long id, MultipartFile fileImg, String roomName, int capacity, float parsedPricing) {
        try {
            Long userId = AuthenUtil.getCurrentUserId();
            Optional<Account> account = accountRepository.findById(userId);
            Venue venue = account.get().getVenue();
            List<Room> roomList = venue.getRoomList();
            for (Room room : roomList) {
                if (room.getId().equals(id)) {
                    room.setRoomImgUrl(fileImg == null ? room.getRoomImgUrl() : firebaseService.uploadImage(fileImg));
                    room.setRoomName(roomName == null ? room.getRoomName() : roomName);
                    room.setCapacity(capacity == 0 ? room.getCapacity() : capacity);
                    room.setPricing(parsedPricing == 0 ? room.getPricing() : parsedPricing);
                    room.setUpdateAt(LocalDateTime.now());
                    room.setVenue(venue);
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
    public ResponseEntity<ResponseObj> enable(Long roomId) {
        Long userId = AuthenUtil.getCurrentUserId();
        Optional<Account> account = accountRepository.findById(userId);
        Venue venue = account.get().getVenue();
        List<Room> roomList = venue.getRoomList();
        for (Room room : roomList) {
            if (room.getId().equals(roomId)) {
                room.setActive(true);
                roomRepository.save(room);
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Enable successfully", room));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Room not found", null));
    }

    @Override
    public ResponseEntity<ResponseObj> delete(Long roomId) {
        try {
            Long userId = AuthenUtil.getCurrentUserId();
            Optional<Account> account = accountRepository.findById(userId);
            Venue venue = account.get().getVenue();
            List<Room> roomList = venue.getRoomList();
            for (Room room : roomList) {
                if (room.getId().equals(roomId)) {
                    room.setActive(false);
                    room.setDeleteAt(LocalDateTime.now());
                    room.setVenue(venue);
                    List<SlotInRoom> slotInRoomList = room.getSlotInRoomList();
                    for (SlotInRoom slotInRoom : slotInRoomList) {
                        slotInRoom.setActive(false);
                        slotInRoom.setDeleteAt(LocalDateTime.now());
                    }
                    roomRepository.save(room);

                    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Room disable successfully", null));

                }
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Room not found", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }

    }


    //thêm
    @Override
    public ResponseEntity<ResponseObj> checkSlotInRoomForCustomer(LocalDate date, Long venueId) {
        try {
            if (date == null) {
                date = LocalDate.now();
            }
            LocalDateTime currentDateTime = LocalDateTime.now();
            ;
            LocalDateTime chooseDateTime = date.atStartOfDay();

            if (currentDateTime.isAfter(chooseDateTime.plusHours(6))) {
                List<SlotInRoom> slotInRoomList = new ArrayList<>();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "The date has expired", slotInRoomList));
            }
            Optional<Venue> venue = venueRepository.findById(venueId);
            List<Room> roomList = venue.get().getRoomList();
            for(Room room: roomList){
                room.getVenue().setRoomList(null);
                room.getVenue().setAccount(null);
                room.setVenueInfo(room.getVenue());

            }
            List<Room> roomListCustomer = new ArrayList<>();
            for (Room room : roomList) {
                if (room.isActive()) {
                    roomListCustomer.add(room);
                }
            }
            if (roomListCustomer.isEmpty())
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "List is empty", null));
            List<PartyBooking> partyBookingList = partyBookingRepository.findAllByDateAndIsActiveIsTrue(date);

            for (Room room : roomListCustomer) {
                List<SlotInRoom> slotInRoomList = room.getSlotInRoomList();
                List<SlotInRoom> slotInRoomValidate = new ArrayList<>();
                for (SlotInRoom slotInRoom : slotInRoomList) {
                    if (slotInRoom.isActive()) {
                        slotInRoomValidate.add(slotInRoom);
                    }
                }

                for (SlotInRoom slotInRoom : slotInRoomValidate) {
                    for (PartyBooking partyBooking : partyBookingList) {
                        if (partyBooking.getSlotInRoom().equals(slotInRoom)) {
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
            if (date == null) {
                date = LocalDate.now();
            }
            Long userId = AuthenUtil.getCurrentUserId();
            Optional<Account> account = accountRepository.findById(userId);
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

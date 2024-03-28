package com.bookingBirthday.bookingbirthdayforkids.service.impl;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.RoomRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.model.Room;
import com.bookingBirthday.bookingbirthdayforkids.model.Services;
import com.bookingBirthday.bookingbirthdayforkids.model.Venue;
import com.bookingBirthday.bookingbirthdayforkids.repository.RoomRepository;
import com.bookingBirthday.bookingbirthdayforkids.repository.VenueRepository;
import com.bookingBirthday.bookingbirthdayforkids.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    VenueRepository venueRepository;
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
        if(roomRepository.existsRoomByRoomName(roomName)){
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(new ResponseObj(HttpStatus.ALREADY_REPORTED.toString(),"Room name has already exist", null));
        }
        Optional<Venue> venue = venueRepository.findById(venueId);
        if(!venue.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Venue does not exist", null));
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
}

package com.bookingBirthday.bookingbirthdayforkids.service.impl;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.SlotRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.model.*;
import com.bookingBirthday.bookingbirthdayforkids.repository.RoomRepository;
import com.bookingBirthday.bookingbirthdayforkids.repository.SlotInRoomRepository;
import com.bookingBirthday.bookingbirthdayforkids.repository.SlotRepository;
import com.bookingBirthday.bookingbirthdayforkids.service.SlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Service
public class SlotServiceImpl implements SlotService {
    @Autowired
    SlotRepository slotRepository;
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    SlotInRoomRepository slotInRoomRepository;
    @Override
    public ResponseEntity<ResponseObj> getAll() {
        List<Slot> slotList = slotRepository.findAllByIsActiveIsTrue();
        if (slotList.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "List is empty", null));

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.OK.toString(), "OK", slotList));
    }

    @Override
    public ResponseEntity<ResponseObj> getAllForHost(){
        List<Slot> slotList = slotRepository.findAll();
        if (slotList.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "List is empty", null));

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.OK.toString(), "OK", slotList));
    }

    @Override
    public ResponseEntity<ResponseObj> getById(Long id) {
        try {
            Optional<Slot> slotList = slotRepository.findById(id);
            if(slotList.isPresent())
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), null, slotList));
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Slot does not exist", null));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }
    @Override
    public ResponseEntity<ResponseObj> getById_ForCustomer(Long id) {
        try {
            Optional<Slot> slot = slotRepository.findById(id);
            if (slot.isPresent() && slot.get().isActive()) {
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", slot));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This slot does not exist", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }


    @Override
    public ResponseEntity<ResponseObj> create(SlotRequest slotRequest){
        if (slotRequest == null || slotRequest.getTimeStart() == null || slotRequest.getTimeStart().isEmpty() ||
                slotRequest.getTimeEnd() == null || slotRequest.getTimeEnd().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Time start and time end must be provided", null));
        }
        Slot slot = new Slot();
        if (isInvalidTimeRange(slotRequest.getTimeStart(), slotRequest.getTimeEnd())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(),"Time start must be earlier than time end", null));
        }
        if (!isMinimumTimeSlot(slotRequest.getTimeStart(), slotRequest.getTimeEnd())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(),"Minimum time slot is 2 hours", null));
        }
        if (isInvalidTimePeriod(slotRequest.getTimeStart(), slotRequest.getTimeEnd())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(),"Cannot create slot between 00:00:00 and 07:59:59", null));
        }

        List<Slot> existingSlots = slotRepository.findAll();
        if (!existingSlots.isEmpty()) {
            Slot lastSlot = existingSlots.get(existingSlots.size() - 1);
            if (!isTimeGapValid(lastSlot.getTimeEnd(), slotRequest.getTimeStart())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(),"Time gap between slots must be at least 1 hour", null));
            }
        }

        slot.setTimeStart(slotRequest.getTimeStart());
        slot.setTimeEnd(slotRequest.getTimeEnd());
        slot.setActive(true);
        slot.setCreateAt(LocalDateTime.now());
        slot.setUpdateAt(LocalDateTime.now());
        slotRepository.save(slot);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(),"Create successful", slot));
    }
    private boolean isInvalidTimeRange(String timeStart, String timeEnd) {
        try {
            LocalTime start = LocalTime.parse(timeStart, DateTimeFormatter.ofPattern("HH:mm:ss"));
            LocalTime end = LocalTime.parse(timeEnd, DateTimeFormatter.ofPattern("HH:mm:ss"));
            return start.isAfter(end);
        } catch (DateTimeParseException e) {
            return true; // If parsing fails, consider it invalid
        }
    }
    private boolean isMinimumTimeSlot(String timeStart, String timeEnd) {
        try {
            LocalTime start = LocalTime.parse(timeStart, DateTimeFormatter.ofPattern("HH:mm:ss"));
            LocalTime end = LocalTime.parse(timeEnd, DateTimeFormatter.ofPattern("HH:mm:ss"));
            return Duration.between(start, end).toHours() >= 2;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
    private boolean isTimeGapValid(String endTimeOfFirstSlot, String startTimeOfSecondSlot) {
        try {
            LocalTime endOfFirstSlot = LocalTime.parse(endTimeOfFirstSlot, DateTimeFormatter.ofPattern("HH:mm:ss"));
            LocalTime startOfSecondSlot = LocalTime.parse(startTimeOfSecondSlot, DateTimeFormatter.ofPattern("HH:mm:ss"));
            return Duration.between(endOfFirstSlot, startOfSecondSlot).toHours() >= 1;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private boolean isInvalidTimePeriod(String timeStart, String timeEnd) {
        try {
            LocalTime start = LocalTime.parse(timeStart, DateTimeFormatter.ofPattern("HH:mm:ss"));
            LocalTime end = LocalTime.parse(timeEnd, DateTimeFormatter.ofPattern("HH:mm:ss"));
            if ((start.isAfter(LocalTime.of(00, 00, 00)) && start.isBefore(LocalTime.of(8, 00, 00))) ||
                    (end.isAfter(LocalTime.of(00, 00, 00)) && end.isBefore(LocalTime.of(8, 00, 00)))) {
                return true;
            }
            return false;
        } catch (DateTimeParseException e) {
            return true; // If parsing fails, consider it invalid
        }
    }
    @Override
    public ResponseEntity<ResponseObj> update(Long id, SlotRequest slotRequest) {
        Optional<Slot> existSlot  = slotRepository.findById(id);
        if (existSlot.isPresent()){
            if (isInvalidTimeRange(slotRequest.getTimeStart(), slotRequest.getTimeEnd())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(),"Time start must be earlier than time end", null));
            }
            if (!isMinimumTimeSlot(slotRequest.getTimeStart(), slotRequest.getTimeEnd())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(),"Minimum time slot is 2 hours", null));
            }
            if (isInvalidTimePeriod(slotRequest.getTimeStart(), slotRequest.getTimeEnd())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(),"Cannot create slot between 00:00:00 and 07:59:59", null));
            }

            existSlot.get().setTimeStart(slotRequest.getTimeStart() == null ? existSlot.get().getTimeStart() : slotRequest.getTimeStart());
            existSlot.get().setTimeEnd(slotRequest.getTimeEnd() == null ? existSlot.get().getTimeEnd() : slotRequest.getTimeEnd());
            existSlot.get().setUpdateAt(LocalDateTime.now());
            slotRepository.save(existSlot.get());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Update successful", existSlot));
        }
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Slot does not exist", null));
    }

    @Override
    public ResponseEntity<ResponseObj> delete(Long id) {
        Optional<Slot> slot = slotRepository.findById(id);
        if (slot.isPresent()){
            slot.get().setActive(false);
            slot.get().setDeleteAt(LocalDateTime.now());
            slotRepository.save(slot.get());
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Delete successful", null));
        }
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "slot does not exist", null));
    }

    @Override
    public ResponseEntity<ResponseObj> addSlotInRoomByRoomId(Long roomId, List<Long> slotId) {
        Room room = roomRepository.findById(roomId).get();
        SlotInRoom slotInRoom = new SlotInRoom();
        ResponseEntity<ResponseObj> response = null;
        for (Long addSlot : slotId){
            Slot slot = slotRepository.findById(addSlot.longValue()).orElse(null);
            if (slot == null) {
                response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Slot not found", null));
                continue;
            }
            SlotInRoom existingSlotInRoom = slotInRoomRepository.findByRoomAndSlot(room, slot);
            if (existingSlotInRoom != null) {
                response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Slot in room already exists", null));
                continue;
            }
            slotInRoom = new SlotInRoom();
            slotInRoom.setRoom(room);
            slotInRoom.setSlot(slot);
            slotInRoom.setActive(true);
            slotInRoom.setCreateAt(LocalDateTime.now());
            slotInRoom.setUpdateAt(LocalDateTime.now());
            slotInRoomRepository.save(slotInRoom);
        }
        if(response == null){
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Create successful", slotInRoom));
        }
        return response;
    }

}

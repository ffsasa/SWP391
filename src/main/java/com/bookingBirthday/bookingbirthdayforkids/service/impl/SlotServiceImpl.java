package com.bookingBirthday.bookingbirthdayforkids.service.impl;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.SlotRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.model.*;
import com.bookingBirthday.bookingbirthdayforkids.model.Package;
import com.bookingBirthday.bookingbirthdayforkids.repository.*;
import com.bookingBirthday.bookingbirthdayforkids.service.SlotService;
import com.bookingBirthday.bookingbirthdayforkids.util.AuthenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
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
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    VenueRepository venueRepository;

    public ResponseEntity<ResponseObj> getAllSlotForCustomer(Long venueId) {
        Optional<Venue> venue = venueRepository.findById(venueId);
        if (!venue.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Venue not found", null));
        }
        Account account = venue.get().getAccount();
        List<Slot> slotList = slotRepository.findAllByIsActiveIsTrueAndAccount(account);
        if (slotList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "List is empty", null));
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", slotList));
    }


    @Override
    public ResponseEntity<ResponseObj> getAllSlotForHost(Long accountId) {
        Long userId = AuthenUtil.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "User not found", null));
        }

        Optional<Account> account = accountRepository.findById(userId);
        if (!account.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "Account not found", null));
        }

        Role role = roleRepository.findByName(RoleEnum.HOST);
        if (!account.get().getRole().equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ResponseObj(HttpStatus.FORBIDDEN.toString(), "User is not a host", null));
        }

        Optional<Account> requestedAccount = accountRepository.findById(accountId);
        if (!requestedAccount.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Account not found", null));
        }

        List<Slot> slotList = slotRepository.findAllByAccountId(accountId);

        if (slotList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "No slots found for this account", null));
        }

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", slotList));
    }


    @Override
    public ResponseEntity<ResponseObj> getByIdForCustomer(Long venueId, Long id) {
        Long userId = AuthenUtil.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "User not found", null));
        }

        Optional<Account> account = accountRepository.findById(userId);
        if (!account.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "Account not found", null));
        }

        Role role = roleRepository.findByName(RoleEnum.CUSTOMER);
        if (!account.get().getRole().equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ResponseObj(HttpStatus.FORBIDDEN.toString(), "User is not a customer", null));
        }

        Optional<Venue> venue = venueRepository.findById(venueId);
        if (!venue.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Venue not found", null));
        }

        try {
            Optional<Slot> slot = slotRepository.findById(id);
            if (slot.isPresent()) {
                if (slot.get().getAccount().getVenue().getId().equals(venueId) && slot.get().isActive()) {
                    return ResponseEntity.status(HttpStatus.ACCEPTED)
                            .body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", slot));
                } else {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(new ResponseObj(HttpStatus.FORBIDDEN.toString(), "You do not have permission to access this slot", null));
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Slot does not exist", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> getByIdForHost(Long accountId, Long id) {
        Long userId = AuthenUtil.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "User not found", null));
        }

        Optional<Account> account = accountRepository.findById(userId);
        if (!account.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "Account not found", null));
        }

        Role role = roleRepository.findByName(RoleEnum.HOST);
        if (!account.get().getRole().equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ResponseObj(HttpStatus.FORBIDDEN.toString(), "User is not a host", null));
        }

        try {
            Optional<Slot> slot = slotRepository.findById(id);
            if (slot.isPresent()) {
                if (slot.get().getAccount().getId().equals(accountId)) {
                    return ResponseEntity.status(HttpStatus.ACCEPTED)
                            .body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", slot));
                } else {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(new ResponseObj(HttpStatus.FORBIDDEN.toString(), "You do not have permission to access this slot", null));
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Slot does not exist", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }


    @Override
    public ResponseEntity<ResponseObj> create(SlotRequest slotRequest) {
        Long userId = AuthenUtil.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "User not found", null));
        }
        Optional<Account> account = accountRepository.findById(userId);
        if (!account.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "Account not found", null));
        }

        Role role = roleRepository.findByName(RoleEnum.HOST);
        if (!account.get().getRole().equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ResponseObj(HttpStatus.FORBIDDEN.toString(), "User is not a host", null));
        }


        if (slotRequest == null || slotRequest.getTimeStart() == null || slotRequest.getTimeStart().isEmpty() ||
                slotRequest.getTimeEnd() == null || slotRequest.getTimeEnd().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Time start and time end must be provided", null));
        }
        if (isInvalidTimeRange(slotRequest.getTimeStart(), slotRequest.getTimeEnd())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Time start must be earlier than time end", null));
        }
        if (!isMinimumTimeSlot(slotRequest.getTimeStart(), slotRequest.getTimeEnd())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Minimum time slot is 2 hours", null));
        }
        if (isInvalidTimePeriod(slotRequest.getTimeStart(), slotRequest.getTimeEnd())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Cannot create slot between 00:00:00 and 07:59:59", null));
        }

        List<Slot> existingSlots = slotRepository.findByAccountId(userId);
        if (!existingSlots.isEmpty()) {
            Slot lastSlot = existingSlots.get(existingSlots.size() - 1);
            if (!isTimeGapValid(lastSlot.getTimeEnd(), slotRequest.getTimeStart())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Time gap between slots must be at least 1 hour", null));
            }
        }
        
        Slot slot = new Slot();
        slot.setTimeStart(slotRequest.getTimeStart());
        slot.setTimeEnd(slotRequest.getTimeEnd());
        slot.setActive(true);
        slot.setCreateAt(LocalDateTime.now());
        slot.setUpdateAt(LocalDateTime.now());
        slot.setAccount(account.get());
        slotRepository.save(slot);

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Create successful", slot));
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

    //fix
    @Override
    public ResponseEntity<ResponseObj> update(Long id, SlotRequest slotRequest) {
        Long userId = AuthenUtil.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "User not found", null));
        }

        Optional<Account> account = accountRepository.findById(userId);
        if (!account.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "Account not found", null));
        }

        Role role = roleRepository.findByName(RoleEnum.HOST);
        if (!account.get().getRole().equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ResponseObj(HttpStatus.FORBIDDEN.toString(), "User is not a host", null));
        }

        Optional<Slot> existSlot = slotRepository.findById(id);
        if (!existSlot.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Slot does not exist", null));
        }

        if (isInvalidTimeRange(slotRequest.getTimeStart(), slotRequest.getTimeEnd())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Time start must be earlier than time end", null));
        }
        if (!isMinimumTimeSlot(slotRequest.getTimeStart(), slotRequest.getTimeEnd())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Minimum time slot is 2 hours", null));
        }
        if (isInvalidTimePeriod(slotRequest.getTimeStart(), slotRequest.getTimeEnd())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Cannot create slot between 00:00:00 and 07:59:59", null));
        }

        Slot slotToUpdate = existSlot.get();
        slotToUpdate.setTimeStart(slotRequest.getTimeStart());
        slotToUpdate.setTimeEnd(slotRequest.getTimeEnd());
        slotToUpdate.setUpdateAt(LocalDateTime.now());
        slotToUpdate.setAccount(account.get());
        slotRepository.save(slotToUpdate);

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Update successful", slotToUpdate));
    }


    //fix
    @Override
    public ResponseEntity<ResponseObj> delete(Long id) {
        Long userId = AuthenUtil.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "User not found", null));
        }

        Optional<Account> account = accountRepository.findById(userId);
        if (!account.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "Account not found", null));
        }

        Role role = roleRepository.findByName(RoleEnum.HOST);
        if (!account.get().getRole().equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ResponseObj(HttpStatus.FORBIDDEN.toString(), "User is not a host", null));
        }

        Optional<Slot> slot = slotRepository.findById(id);
        if (!slot.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Slot does not exist", null));
        }

        if (!slot.get().getAccount().getId().equals(account.get().getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Account id not correct", null));
        }

        slot.get().setActive(false);
        slot.get().setAccount(account.get());
        slot.get().setDeleteAt(LocalDateTime.now());
        slotRepository.save(slot.get());

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObj(HttpStatus.OK.toString(), "Delete successful", null));
    }


    @Override
    public ResponseEntity<ResponseObj> addSlotInRoomByRoomId(Long roomId, List<Long> slotId) {
        Long userId = AuthenUtil.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "User not found", null));
        }

        Optional<Account> account = accountRepository.findById(userId);
        if (!account.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "Account not found", null));
        }

        Role role = roleRepository.findByName(RoleEnum.HOST);
        if (!account.get().getRole().equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ResponseObj(HttpStatus.FORBIDDEN.toString(), "User is not a host", null));
        }

        Room room = roomRepository.findById(roomId).orElse(null);
        if (room == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Room not found", null));
        }

        ResponseEntity<ResponseObj> response = null;
        for (Long addSlot : slotId) {
            Slot slot = slotRepository.findById(addSlot).orElse(null);
            if (slot == null) {
                response = ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Slot not found", null));
                continue;
            }

            SlotInRoom existingSlotInRoom = slotInRoomRepository.findByRoomAndSlot(room, slot);
            if (existingSlotInRoom != null) {
                response = ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Slot in room already exists", null));
                continue;
            }

            SlotInRoom slotInRoom = new SlotInRoom();
            slotInRoom.setRoom(room);
            slotInRoom.setSlot(slot);
            slotInRoom.getSlot().setAccount(account.get());
            slotInRoom.setActive(true);
            slotInRoom.setCreateAt(LocalDateTime.now());
            slotInRoom.setUpdateAt(LocalDateTime.now());
            slotInRoomRepository.save(slotInRoom);
        }

        if (response == null) {
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Create successful", null));
        }

        return response;
    }


}

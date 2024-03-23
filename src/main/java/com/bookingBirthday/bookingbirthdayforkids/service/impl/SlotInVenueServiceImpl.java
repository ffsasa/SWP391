package com.bookingBirthday.bookingbirthdayforkids.service.impl;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.SlotInVenueRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.model.Slot;
import com.bookingBirthday.bookingbirthdayforkids.model.SlotInVenue;
import com.bookingBirthday.bookingbirthdayforkids.model.Venue;
import com.bookingBirthday.bookingbirthdayforkids.repository.SlotInVenueRepository;
import com.bookingBirthday.bookingbirthdayforkids.repository.SlotRepository;
import com.bookingBirthday.bookingbirthdayforkids.repository.VenueRepository;
import com.bookingBirthday.bookingbirthdayforkids.service.SlotInVenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SlotInVenueServiceImpl implements SlotInVenueService {
    @Autowired
    SlotRepository slotRepository;
    @Autowired
    VenueRepository venueRepository;
    @Autowired
    SlotInVenueRepository slotInVenueRepository;
    @Override
    public ResponseEntity<ResponseObj> create(SlotInVenueRequest slotInVenueRequest) {
        Optional<Slot> slot = slotRepository.findById(slotInVenueRequest.getSlot_id());
        Optional<Venue> venue = venueRepository.findById(slotInVenueRequest.getVenue_id());
        if (!slot.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Slot does not exist", null));
        }
        if(!venue.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Venue does not exist", null));
        }
        if(slotInVenueRepository.existsBySlotIdAndVenueId(slotInVenueRequest.getSlot_id(), slotInVenueRequest.getVenue_id())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Slot in venue existed", null));
        }
        SlotInVenue slotInVenue = new SlotInVenue();
        slotInVenue.setSlot(slot.get());
        slotInVenue.setVenue(venue.get());
        slotInVenue.setCreateAt(LocalDateTime.now());
        slotInVenue.setActive(true);
        slotInVenue.setUpdateAt(LocalDateTime.now());
        slotInVenueRepository.save(slotInVenue);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseObj(HttpStatus.CREATED.toString(), "Create successful", slotInVenue));
    }

    @Override
    public ResponseEntity<ResponseObj> disableSlotInVenue(Long id) {
        Optional<SlotInVenue> slotInVenue = slotInVenueRepository.findById(id);
        if(slotInVenue.isPresent()){
            slotInVenue.get().setActive(false);
            slotInVenueRepository.save(slotInVenue.get());
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Disable successful", slotInVenue));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Slot in venue does not exist", null));
    }

    @Override
    public ResponseEntity<ResponseObj> activeSlotInVenue(Long id) {
        Optional<SlotInVenue> slotInVenue = slotInVenueRepository.findById(id);
        if(slotInVenue.isPresent()){
            if (!slotInVenue.get().getVenue().isActive())
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Venue is not active. You can not enable slotInVenue", null));

            slotInVenue.get().setActive(true);
            slotInVenue.get().setUpdateAt(LocalDateTime.now());
            slotInVenueRepository.save(slotInVenue.get());
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Active successful", slotInVenue));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Slot in venue does not exist", null));
    }
}

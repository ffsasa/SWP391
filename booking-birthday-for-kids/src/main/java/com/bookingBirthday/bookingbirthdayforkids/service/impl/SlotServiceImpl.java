package com.bookingBirthday.bookingbirthdayforkids.service.impl;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.PartyDatedRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.request.SlotRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.model.PartyDated;
import com.bookingBirthday.bookingbirthdayforkids.model.Slot;
import com.bookingBirthday.bookingbirthdayforkids.repository.PartyDatedRepository;
import com.bookingBirthday.bookingbirthdayforkids.repository.SlotRepository;
import com.bookingBirthday.bookingbirthdayforkids.service.SlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SlotServiceImpl implements SlotService {
    @Autowired
    SlotRepository slotRepository;
    @Override
    public ResponseEntity<ResponseObj> getAll() {
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
    public ResponseEntity<ResponseObj> create(SlotRequest slotRequest){
        Slot slot = new Slot() ;
        slot.setTimeStart(slotRequest.getTimeStart());
        slot.setTimeEnd(slotRequest.getTimeEnd());
        slotRepository.save(slot);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(),"Create successful", slot));
    }

    @Override
    public ResponseEntity<ResponseObj> update(Long id, SlotRequest slotRequest) {
        Optional<Slot> existSlot  = slotRepository.findById(id);
        if (existSlot.isPresent()){
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
            slot.get().setUpdateAt(LocalDateTime.now());
            slotRepository.save(slot.get());
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Delete successful", null));
        }
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "slot does not exist", null));
    }
}

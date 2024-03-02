package com.bookingBirthday.bookingbirthdayforkids.service.impl;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.PartyBookingRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.model.PartyBooking;
import com.bookingBirthday.bookingbirthdayforkids.model.StatusEnum;
import com.bookingBirthday.bookingbirthdayforkids.repository.PartyBookingRepository;
import com.bookingBirthday.bookingbirthdayforkids.service.PartyBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PartyBookingServiceImpl implements PartyBookingService {

    @Autowired
    PartyBookingRepository partyBookingRepository;

    @Override
    public ResponseEntity<ResponseObj> getAll() {
        try {
            List<PartyBooking> partyBookingList = partyBookingRepository.findAll();
            if(partyBookingList.isEmpty()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "List is empty", null));
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", partyBookingList));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> getById(Long id) {
        try {
            Optional<PartyBooking> partyBooking = partyBookingRepository.findById(id);
            if(partyBooking.isPresent()){
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", partyBooking));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This party booking does not exist", null));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> create(PartyBookingRequest partyBookingRequest) {
        try {

            PartyBooking partyBooking = new PartyBooking();

            partyBooking.setKidName(partyBookingRequest.getKidName());
            partyBooking.setKidDOB(partyBookingRequest.getKidDOB());
            partyBooking.setEmail(partyBookingRequest.getEmail());
            partyBooking.setPhone(partyBookingRequest.getPhone());
            partyBooking.setStatus(StatusEnum.PENDING);
            partyBooking.setActive(true);
            partyBooking.setCreateAt(LocalDateTime.now());
            partyBooking.setUpdateAt(LocalDateTime.now());

            partyBookingRepository.save(partyBooking);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Create successful", partyBooking));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> update(Long id, PartyBookingRequest partyBookingRequest) {
        try {
            Optional<PartyBooking> existPartyBooking = partyBookingRepository.findById(id);
            if (existPartyBooking.isPresent()){
                existPartyBooking.get().setKidName(partyBookingRequest.getKidName() == null ? existPartyBooking.get().getKidName() : partyBookingRequest.getKidName());
                existPartyBooking.get().setKidDOB(partyBookingRequest.getKidDOB() == null ? existPartyBooking.get().getKidDOB() : partyBookingRequest.getKidDOB());
                existPartyBooking.get().setEmail(partyBookingRequest.getEmail() == null ? existPartyBooking.get().getEmail() : partyBookingRequest.getEmail());
                existPartyBooking.get().setPhone(partyBookingRequest.getPhone() == 0 ? existPartyBooking.get().getPhone() : partyBookingRequest.getPhone());
                existPartyBooking.get().setUpdateAt(LocalDateTime.now());
                partyBookingRepository.save(existPartyBooking.get());
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Update successful", existPartyBooking));
            }
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This party booking does not exist", null));

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> delete(Long id) {
        {
            try {
                Optional<PartyBooking> partyBooking = partyBookingRepository.findById(id);
                if (partyBooking.isPresent()){
                    partyBooking.get().setActive(false);
                    partyBooking.get().setDeleteAt(LocalDateTime.now());
                    partyBookingRepository.save(partyBooking.get());
                    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Delete successful", null));
                } else
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This party booking does not exist", null));
            } catch (Exception e){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
            }
        }
    }
}

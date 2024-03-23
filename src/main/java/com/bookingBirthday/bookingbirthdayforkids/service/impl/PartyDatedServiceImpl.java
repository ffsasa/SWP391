package com.bookingBirthday.bookingbirthdayforkids.service.impl;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.PartyDatedRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.model.*;
import com.bookingBirthday.bookingbirthdayforkids.repository.PartyBookingRepository;
import com.bookingBirthday.bookingbirthdayforkids.repository.PartyDatedRepository;
import com.bookingBirthday.bookingbirthdayforkids.repository.SlotInVenueRepository;
import com.bookingBirthday.bookingbirthdayforkids.repository.UpgradeServiceRepository;
import com.bookingBirthday.bookingbirthdayforkids.service.PartyDatedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PartyDatedServiceImpl implements PartyDatedService {
    @Autowired
    PartyDatedRepository partyDatedRepository;
    @Autowired
    SlotInVenueRepository slotInVenueRepository;
    @Autowired
    PartyBookingRepository partyBookingRepository;
    @Autowired
    UpgradeServiceRepository upgradeServiceRepository;
    @Override
    public ResponseEntity<ResponseObj> getAll() {
        List<PartyDated> partyDatedList = partyDatedRepository.findAllByIsActiveIsTrue();
        if (partyDatedList.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "List is empty", null));

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.OK.toString(), "OK", partyDatedList));
    }

    public ResponseEntity<ResponseObj> getAllForHost(){
        List<PartyDated> partyDatedList = partyDatedRepository.findAll();
        if (partyDatedList.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "List is empty", null));

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.OK.toString(), "OK", partyDatedList));
    }


    @Override
    public ResponseEntity<ResponseObj> getById(Long id) {
        try {
            Optional<PartyDated> partyDatedList = partyDatedRepository.findById(id);
            if(partyDatedList.isPresent())
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), null, partyDatedList));
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Slot does not exist", null));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }
    @Override
    public ResponseEntity<ResponseObj> getById_ForCustomer(Long id) {
        try {
            Optional<PartyDated> partyDatedList = partyDatedRepository.findById(id);
            if (partyDatedList.isPresent() && partyDatedList.get().isActive()) {
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", partyDatedList));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This partydated does not exist", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }
    @Override
    public ResponseEntity<ResponseObj> update(Long id, PartyDatedRequest partyDatedRequest) {
        SlotInVenue slotInVenue = slotInVenueRepository.findById(partyDatedRequest.getSlotInVenueId()).get();
        Optional<PartyDated> existPartyDated  = partyDatedRepository.findById(id);
        if (existPartyDated.isPresent()){
            existPartyDated.get().setSlotInVenue(slotInVenue == null ? existPartyDated.get().getSlotInVenue() : slotInVenue);
            existPartyDated.get().setDate(partyDatedRequest.getDate() == null ? existPartyDated.get().getDate() : partyDatedRequest.getDate());
            existPartyDated.get().setUpdateAt(LocalDateTime.now());
            partyDatedRepository.save(existPartyDated.get());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Update successful", existPartyDated));
        }
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "PartyDated does not exist", null));

    }

    @Override
    public ResponseEntity<ResponseObj> delete(Long id) {
        Optional<PartyDated> partyDated = partyDatedRepository.findById(id);
        if (partyDated.isPresent()){
            partyDated.get().setActive(false);
            partyDated.get().setDeleteAt(LocalDateTime.now());
            partyDatedRepository.save(partyDated.get());
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Delete successful", null));
        }
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "PartyDated does not exist", null));
    }

    @Override
    public ResponseEntity<ResponseObj> getPartyBookingByPartyDateId(Long id) {
        Optional<PartyDated> partyDated = partyDatedRepository.findById(id);
        if (partyDated.isPresent()){
            PartyBooking partyBooking = partyDated.get().getPartyBooking();
            SlotInVenue slotInVenue = partyBooking.getPartyDated().getSlotInVenue();
            partyBooking.setSlotInVenueObject(slotInVenue);
            slotInVenue.setPartyDatedObject(partyDated.get());
            Venue venue = slotInVenue.getVenue();
            venue.setSlotInVenueList(null);
            partyBooking.setVenue(venue);

            float Upricing = 0;
            List<UpgradeService> upgradeService = upgradeServiceRepository.findAllByPartyBookingId(partyBooking.getId());
            for (UpgradeService upgradeService1 : upgradeService){
                Upricing = Upricing + upgradeService1.getPricing();
            }
            partyBooking.setPricingTotal(partyBooking.getPackageInVenue().getApackage().getPricing()+Upricing);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", partyBooking));
        }
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "PartyDated does not exist", null));
    }
}

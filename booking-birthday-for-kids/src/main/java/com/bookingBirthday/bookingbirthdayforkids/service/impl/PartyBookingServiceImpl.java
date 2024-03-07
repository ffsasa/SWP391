package com.bookingBirthday.bookingbirthdayforkids.service.impl;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.PartyBookingRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.model.*;
import com.bookingBirthday.bookingbirthdayforkids.repository.*;
import com.bookingBirthday.bookingbirthdayforkids.service.PartyBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PartyBookingServiceImpl implements PartyBookingService {

    @Autowired
    PartyBookingRepository partyBookingRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    VenueRepository venueRepository;
    @Autowired
    ThemeRepository themeRepository;
    @Autowired
    UpgradeServiceRepository upgradeServiceRepository;
    @Autowired
    ServicesRepository servicesRepository;
    @Override
    public ResponseEntity<ResponseObj> getAll() {
        try {
            List<PartyBooking> partyBookingList = partyBookingRepository.findAll();
            if(partyBookingList.isEmpty()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "List is empty", null));
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", partyBookingList));
        } catch (Exception e) {
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
            Account account = accountRepository.findById(partyBookingRequest.getAccountId()).get();
            Venue venue = venueRepository.findById(partyBookingRequest.getVenueId()).get();
            Theme theme = themeRepository.findById(partyBookingRequest.getThemeId()).get();

            PartyBooking partyBooking = new PartyBooking();

            partyBooking.setKidName(partyBookingRequest.getKidName());
            partyBooking.setKidDOB(partyBookingRequest.getKidDOB());
            partyBooking.setEmail(partyBookingRequest.getEmail());
            partyBooking.setPhone(partyBookingRequest.getPhone());
            partyBooking.setStatus(StatusEnum.PENDING);
            partyBooking.setActive(true);
            partyBooking.setCreateAt(LocalDateTime.now());
            partyBooking.setUpdateAt(LocalDateTime.now());
            partyBooking.setAccount(account);
            partyBooking.setTheme(theme);
            partyBooking.setVenue(venue);

            partyBookingRepository.save(partyBooking);

            Map<String, Integer> dataUpgrade = partyBookingRequest.getDataUpgrade();

            if(dataUpgrade != null){
                for(Map.Entry<String, Integer> entry : dataUpgrade.entrySet()){
                    String serviceId = entry.getKey();
                    int count = entry.getValue();
                    Optional<Services> services = servicesRepository.findById(Long.valueOf(serviceId));

                    if(services.isEmpty()){
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This service does not exist", null));
                    }

                    UpgradeService upgradeService = new UpgradeService();

                    upgradeService.setCount(count);
                    upgradeService.setPricing(count*services.get().getPricing());
                    upgradeService.setActive(true);
                    upgradeService.setCreateAt(LocalDateTime.now());
                    upgradeService.setUpdateAt(LocalDateTime.now());
                    upgradeService.setPartyBooking(partyBooking);
                    upgradeService.setServices(services.get());
                    upgradeServiceRepository.save(upgradeService);
                }
            }

            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Create successful", partyBooking));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> update(Long id, PartyBookingRequest partyBookingRequest) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseObj> delete(Long id) {
        return null;
    }
}

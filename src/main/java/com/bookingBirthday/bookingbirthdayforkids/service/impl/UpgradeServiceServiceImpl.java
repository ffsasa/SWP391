package com.bookingBirthday.bookingbirthdayforkids.service.impl;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.UpgradeServiceRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.model.PartyBooking;
import com.bookingBirthday.bookingbirthdayforkids.model.Services;
import com.bookingBirthday.bookingbirthdayforkids.model.UpgradeService;
import com.bookingBirthday.bookingbirthdayforkids.repository.PartyBookingRepository;
import com.bookingBirthday.bookingbirthdayforkids.repository.ServicesRepository;
import com.bookingBirthday.bookingbirthdayforkids.repository.UpgradeServiceRepository;
import com.bookingBirthday.bookingbirthdayforkids.service.UpgradeServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UpgradeServiceServiceImpl implements UpgradeServiceService {

    @Autowired
    UpgradeServiceRepository upgradeServiceRepository;
    @Autowired
    PartyBookingRepository partyBookingRepository;
    @Autowired
    ServicesRepository servicesRepository;

    @Override
    public ResponseEntity<ResponseObj> getAll() {
        try {
            List<UpgradeService> upgradeServiceList = upgradeServiceRepository.findAllByIsActiveIsTrue();
            if(upgradeServiceList.isEmpty()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "List is empty", null));
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", upgradeServiceList));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> getById(Long id) {
        try {
            Optional<UpgradeService> theme = upgradeServiceRepository.findById(id);
            if(theme.isPresent()){
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", theme));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This upgrade service does not exist", null));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> update(Long id, UpgradeServiceRequest upgradeServiceRequest) {
        try {
            Optional<UpgradeService> existUpgradeService = upgradeServiceRepository.findById(id);
            if (existUpgradeService.isPresent()){
                Services services = existUpgradeService.get().getServices();
                existUpgradeService.get().setCount(upgradeServiceRequest.getCount() == 0 ? existUpgradeService.get().getCount() : upgradeServiceRequest.getCount());
                existUpgradeService.get().setPricing(upgradeServiceRequest.getCount() == 0 ? existUpgradeService.get().getPricing() : upgradeServiceRequest.getCount()*services.getPricing());
                existUpgradeService.get().setUpdateAt(LocalDateTime.now());
                upgradeServiceRepository.save(existUpgradeService.get());
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Update successful", existUpgradeService));
            }
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This upgrade service does not exist", null));

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> delete(Long id) {
        try {
            Optional<UpgradeService> upgradeService = upgradeServiceRepository.findById(id);
            if (upgradeService.isPresent()){
                upgradeService.get().setActive(false);
                upgradeService.get().setDeleteAt(LocalDateTime.now());
                upgradeServiceRepository.save(upgradeService.get());
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Delete successful", null));
            } else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This upgrade service does not exist", null));

        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }
}

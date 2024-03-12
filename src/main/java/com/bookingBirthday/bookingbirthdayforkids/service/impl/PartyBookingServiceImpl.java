package com.bookingBirthday.bookingbirthdayforkids.service.impl;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.PartyBookingRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.model.*;
import com.bookingBirthday.bookingbirthdayforkids.model.Package;
import com.bookingBirthday.bookingbirthdayforkids.repository.*;
import com.bookingBirthday.bookingbirthdayforkids.service.PartyBookingService;
import com.bookingBirthday.bookingbirthdayforkids.util.AuthenUtil;
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
    @Autowired
    PartyDatedRepository partyDatedRepository;
    @Autowired
    SlotInVenueRepository slotInVenueRepository;
    @Autowired
    PackageRepository packageRepository;

    @Override
    public ResponseEntity<ResponseObj> getAll() {
        try {
            List<PartyBooking> partyBookingList = partyBookingRepository.findAll();
            if (partyBookingList.isEmpty()) {
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
            if (partyBooking.isPresent()) {
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", partyBooking));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This party booking does not exist", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> create(PartyBookingRequest partyBookingRequest) {
        try {
            Optional<SlotInVenue> slotInVenue = slotInVenueRepository.findById(partyBookingRequest.getSlotInVenueId());
            if(!slotInVenue.isPresent()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Slot in venue does not exist", null));
            }
            Venue venue = slotInVenue.get().getVenue();
            Theme theme = themeRepository.findById(partyBookingRequest.getThemeId()).get();
            Package aPackage = packageRepository.findById(partyBookingRequest.getPackageId()).get();
            Long userId = AuthenUtil.getCurrentUserId();
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "400", null));
            }
            Account account = accountRepository.findById(userId).get();

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
            partyBooking.setApackage(aPackage);

            PartyDated partyDated = new PartyDated();
            partyDated.setDate(partyBookingRequest.getDate());
            partyDated.setSlotInVenue(slotInVenue.get());
            partyDated.setActive(true);
            partyDated.setCreateAt(LocalDateTime.now());
            partyDated.setUpdateAt(LocalDateTime.now());
            partyDatedRepository.save(partyDated);

            partyBooking.setVenue(venue);
            partyBooking.setPartyDated(partyDated);
            partyBookingRepository.save(partyBooking);



            Map<String, Integer> dataUpgrade = partyBookingRequest.getDataUpgrade();

            if (dataUpgrade != null) {
                for (Map.Entry<String, Integer> entry : dataUpgrade.entrySet()) {
                    String serviceId = entry.getKey();
                    int count = entry.getValue();
                    Optional<Services> services = servicesRepository.findById(Long.valueOf(serviceId));

                    if (services.isEmpty()) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This service does not exist", null));
                    }

                    UpgradeService upgradeService = new UpgradeService();

                    upgradeService.setCount(count);
                    upgradeService.setPricing(count * services.get().getPricing());
                    upgradeService.setActive(true);
                    upgradeService.setCreateAt(LocalDateTime.now());
                    upgradeService.setUpdateAt(LocalDateTime.now());
                    upgradeService.setPartyBooking(partyBooking);
                    upgradeService.setServices(services.get());
                    upgradeServiceRepository.save(upgradeService);
                }
            }

            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Create successful", partyBooking));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> update(Long id, PartyBookingRequest partyBookingRequest) {
        try {
            Long userId = AuthenUtil.getCurrentUserId();
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "400", null));
            }

            Account account = accountRepository.findById(userId).get();
            Optional<PartyBooking> existPartyBooking = partyBookingRepository.findById(id);
            if (existPartyBooking.isPresent()) {
                if (!existPartyBooking.get().getAccount().getId().equals(account.getId())) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseObj(HttpStatus.FORBIDDEN.toString(), "User not permission to update this booking", null));
                }
                Optional<PartyDated> existPartyDated = partyDatedRepository.findPartyDatedByPartyBookingId(id);
                SlotInVenue slotInVenue = slotInVenueRepository.findById(partyBookingRequest.getSlotInVenueId()).get();
                existPartyDated.get().setSlotInVenue(slotInVenue);
                Package aPackage = packageRepository.findById(partyBookingRequest.getPackageId()).get();
                existPartyBooking.get().setApackage(aPackage);

                existPartyBooking.get().setKidName(partyBookingRequest.getKidName() == null ? existPartyBooking.get().getKidName() : partyBookingRequest.getKidName());
                existPartyBooking.get().setKidDOB(partyBookingRequest.getKidDOB() == null ? existPartyBooking.get().getKidDOB() : partyBookingRequest.getKidDOB());
                existPartyBooking.get().setEmail(partyBookingRequest.getEmail() == null ? existPartyBooking.get().getEmail() : partyBookingRequest.getEmail());
                existPartyBooking.get().setPhone(partyBookingRequest.getPhone() == null ? existPartyBooking.get().getPhone() : partyBookingRequest.getPhone());
                existPartyBooking.get().setUpdateAt(LocalDateTime.now());

                existPartyBooking.get().setPartyDated(existPartyDated.get());
                partyBookingRepository.save(existPartyBooking.get());

                Map<String, Integer> dataUpgrade = partyBookingRequest.getDataUpgrade();
                if (dataUpgrade != null) {
                    for (Map.Entry<String, Integer> entry : dataUpgrade.entrySet()) {
                        String serviceId = entry.getKey();
                        int count = entry.getValue();

                        Optional<Services> services = servicesRepository.findById(Long.valueOf(serviceId));
                        if (services.isEmpty()) {
                            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This service does not exist", null));
                        }

                        Optional<UpgradeService> existingUpgradeService = upgradeServiceRepository.findByPartyBookingAndServices(existPartyBooking.get(), services.get());
                        if (existingUpgradeService.isPresent()) {
                            UpgradeService upgradeService = existingUpgradeService.get();
                            upgradeService.setCount(count);
                            upgradeService.setPricing(count * services.get().getPricing());
                            upgradeService.setActive(true);
                            upgradeService.setCreateAt(LocalDateTime.now());
                            upgradeService.setUpdateAt(LocalDateTime.now());
                            upgradeService.setPartyBooking(existPartyBooking.get());
                            upgradeService.setServices(services.get());
                            upgradeServiceRepository.save(upgradeService);
                        }
                        else {
                            UpgradeService newUpgradeService = new UpgradeService();
                            newUpgradeService.setCount(count);
                            newUpgradeService.setPricing(count * services.get().getPricing());
                            newUpgradeService.setActive(true);
                            newUpgradeService.setCreateAt(LocalDateTime.now());
                            newUpgradeService.setUpdateAt(LocalDateTime.now());
                            newUpgradeService.setPartyBooking(existPartyBooking.get());
                            newUpgradeService.setServices(services.get());
                            upgradeServiceRepository.save(newUpgradeService);
                        }
                    }
                }
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Update successful", existPartyBooking));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "User does not exist", null));
    }

    @Override
    public ResponseEntity<ResponseObj> delete(Long id) {
        Optional<PartyBooking> partyBooking = partyBookingRepository.findById(id);
        if (partyBooking.isPresent()){
            partyBooking.get().setActive(false);
            partyBooking.get().setDeleteAt(LocalDateTime.now());
            partyBookingRepository.save(partyBooking.get());
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Delete successful", null));
        }
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Party booking does not exist", null));
    }


}

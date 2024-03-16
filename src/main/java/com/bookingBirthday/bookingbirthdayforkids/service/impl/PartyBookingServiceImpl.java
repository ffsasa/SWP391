package com.bookingBirthday.bookingbirthdayforkids.service.impl;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.PartyBookingRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.request.UpgradeServiceRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.model.*;
import com.bookingBirthday.bookingbirthdayforkids.repository.*;
import com.bookingBirthday.bookingbirthdayforkids.service.PartyBookingService;
import com.bookingBirthday.bookingbirthdayforkids.util.AuthenUtil;
import jakarta.validation.Valid;
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
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ThemeInVenueRepository themeInVenueRepository;
    @Autowired
    PackageInVenueRepository packageInVenueRepository;
    @Autowired
    UpgradeServiceRepository upgradeServiceRepository;
    @Autowired
    ServicesRepository servicesRepository;
    @Autowired
    PartyDatedRepository partyDatedRepository;
    @Autowired
    SlotInVenueRepository slotInVenueRepository;

    @Override
    public ResponseEntity<ResponseObj> getAllByUser() {
        try {
            Long userId = AuthenUtil.getCurrentUserId();
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "400", null));
            }
            List<PartyBooking> partyBookingList = partyBookingRepository.findAllByIsActiveIsTrueAndAccountId(userId);
            if (partyBookingList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "List is empty", null));
            }
            for (PartyBooking partyBooking : partyBookingList) {
                partyBooking.getPartyDated().setSlotObject(partyBooking.getPartyDated().getSlotInVenue().getSlot());
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", partyBookingList));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> getAll() {
        try {
            List<PartyBooking> partyBookingList = partyBookingRepository.findAllByIsActiveIsTrue();
            if (partyBookingList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "List is empty", null));
            }
            for (PartyBooking partyBooking : partyBookingList) {
                partyBooking.getPartyDated().setSlotObject(partyBooking.getPartyDated().getSlotInVenue().getSlot());
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
                PartyBooking partyBookingResult = partyBooking.get();
                partyBookingResult.getPartyDated().setSlotObject(partyBookingResult.getPartyDated().getSlotInVenue().getSlot());
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", partyBookingResult));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This party booking does not exist", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> create(PartyBookingRequest partyBookingRequest) {
        try {
            Long userId = AuthenUtil.getCurrentUserId();
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "400", null));
            }
            Account account = accountRepository.findById(userId).get();

            Optional<ThemeInVenue> themeInVenue = themeInVenueRepository.findById(partyBookingRequest.getThemeInVenueId());
            Optional<PackageInVenue> packageInVenue = packageInVenueRepository.findById(partyBookingRequest.getPackageInVenueId());
            Optional<SlotInVenue> slotInVenue = slotInVenueRepository.findById(partyBookingRequest.getSlotInVenueId());
            if (slotInVenue.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Slot in venue does not exist", null));
            }
            if (themeInVenue.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Theme in venue does not exist", null));
            }
            if (packageInVenue.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Package in venue does not exist", null));
            }

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
            partyBooking.setThemeInVenue(themeInVenue.get());
            partyBooking.setPackageInVenue(packageInVenue.get());
            //PartyDated
            PartyDated partyDate = new PartyDated();
            partyDate.setDate(partyBookingRequest.getDate());
            partyDate.setSlotInVenue(slotInVenue.get());
            partyDate.setActive(true);
            partyDate.setCreateAt(LocalDateTime.now());
            partyDate.setUpdateAt(LocalDateTime.now());
            partyDatedRepository.save(partyDate);

            partyBooking.setPartyDated(partyDate);
            partyBookingRepository.save(partyBooking);

            List<UpgradeServiceRequest> dataUpgrade = partyBookingRequest.getDataUpgrade();
            for (UpgradeServiceRequest data : dataUpgrade) {
                UpgradeService upgradeService = new UpgradeService();
                Optional<Services> optionalService = servicesRepository.findById(data.getServiceId());
                if (optionalService.isPresent()) {
                    Services service = optionalService.get();
                    upgradeService.setPricing(data.getCount() * service.getPricing());
                    upgradeService.setServices(service);
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Service does not exist", null));
                }
                upgradeService.setCount(data.getCount());
                upgradeService.setActive(true);
                upgradeService.setCreateAt(LocalDateTime.now());
                upgradeService.setUpdateAt(LocalDateTime.now());
                upgradeService.setPartyBooking(partyBooking);
                upgradeServiceRepository.save(upgradeService);
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
                if (partyBookingRequest.getDate() != null || partyBookingRequest.getSlotInVenueId() != null) {
                    Optional<SlotInVenue> optionalSlotInVenue = slotInVenueRepository.findById(partyBookingRequest.getSlotInVenueId());
                    if (optionalSlotInVenue.isPresent()) {
                        SlotInVenue slotInVenue = optionalSlotInVenue.get();
                        existPartyDated.get().setSlotInVenue(slotInVenue);
                    } else {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Slot in venue does not exist", null));
                    }
                    existPartyDated.get().setDate(partyBookingRequest.getDate());
                    existPartyDated.get().setActive(true);
                    existPartyDated.get().setCreateAt(LocalDateTime.now());
                    existPartyDated.get().setUpdateAt(LocalDateTime.now());
                    partyDatedRepository.save(existPartyDated.get());
                }

                if (partyBookingRequest.getThemeInVenueId() != null) {
                    Optional<ThemeInVenue> optionalThemeInVenue = themeInVenueRepository.findById(partyBookingRequest.getThemeInVenueId());
                    if (optionalThemeInVenue.isPresent()) {
                        ThemeInVenue themeInVenue = optionalThemeInVenue.get();
                        existPartyBooking.get().setThemeInVenue(themeInVenue);
                    } else {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Theme in venue does not exist", null));
                    }
                } else {
                    existPartyBooking.get().setThemeInVenue(existPartyBooking.get().getThemeInVenue());
                }

                if (partyBookingRequest.getPackageInVenueId() != null) {
                    Optional<PackageInVenue> optionalPackageInVenue = packageInVenueRepository.findById(partyBookingRequest.getPackageInVenueId());
                    if (optionalPackageInVenue.isPresent()) {
                        PackageInVenue packageInVenue = optionalPackageInVenue.get();
                        existPartyBooking.get().setPackageInVenue(packageInVenue);
                    } else {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Package in venue does not exist", null));
                    }
                } else {
                    existPartyBooking.get().setPackageInVenue(existPartyBooking.get().getPackageInVenue());
                }

                existPartyBooking.get().setKidName(partyBookingRequest.getKidName() == null ? existPartyBooking.get().getKidName() : partyBookingRequest.getKidName());
                existPartyBooking.get().setKidDOB(partyBookingRequest.getKidDOB() == null ? existPartyBooking.get().getKidDOB() : partyBookingRequest.getKidDOB());
                existPartyBooking.get().setEmail(partyBookingRequest.getEmail() == null ? existPartyBooking.get().getEmail() : partyBookingRequest.getEmail());
                existPartyBooking.get().setPhone(partyBookingRequest.getPhone() == null ? existPartyBooking.get().getPhone() : partyBookingRequest.getPhone());
                existPartyBooking.get().setUpdateAt(LocalDateTime.now());

                partyBookingRepository.save(existPartyBooking.get());

                List<UpgradeServiceRequest> dataUpgrade = partyBookingRequest.getDataUpgrade();
                if (dataUpgrade != null) {
                    for (UpgradeServiceRequest data : dataUpgrade) {
                        Long serviceId = data.getServiceId();
                        int count = data.getCount();

                        Optional<Services> services = servicesRepository.findById(serviceId);
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
                        } else {
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
        try {
            Optional<PartyBooking> partyBooking = partyBookingRepository.findById(id);
            if (partyBooking.isPresent()) {
                partyBooking.get().setActive(false);
                partyBooking.get().setDeleteAt(LocalDateTime.now());
                partyBookingRepository.save(partyBooking.get());
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Delete successful", null));
            } else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This party booking does not exist", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }
}

package com.bookingBirthday.bookingbirthdayforkids.service.impl;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.PartyBookingRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.request.UpgradeServiceRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.model.*;
import com.bookingBirthday.bookingbirthdayforkids.repository.*;
import com.bookingBirthday.bookingbirthdayforkids.service.PartyBookingService;
import com.bookingBirthday.bookingbirthdayforkids.util.AuthenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class PartyBookingServiceImpl implements PartyBookingService {

    @Autowired
    PartyBookingRepository partyBookingRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    PackageInVenueRepository packageInVenueRepository;
    @Autowired
    UpgradeServiceRepository upgradeServiceRepository;
    @Autowired
    ServicesRepository servicesRepository;
    @Autowired
    PartyDatedRepository partyDatedRepository;
    @Autowired
    SlotInRoomRepository slotInRoomRepository;
    @Autowired
    ReviewRepository reviewRepository;

//    @Override
//    public ResponseEntity<ResponseObj> getAllByUser() {
//        try {
//            Long userId = AuthenUtil.getCurrentUserId();
//            if (userId == null) {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "400", null));
//            }
//            List<PartyBooking> partyBookingList = partyBookingRepository.findAllByIsActiveIsTrueAndAccountId(userId);
//            for (PartyBooking partyBooking : partyBookingList) {
//                SlotInRoom slotInRoom = partyBooking.getPartyDated().getSlotInRoom();
//                partyBooking.setSlotInRoomObject(slotInRoom);
//                partyBooking.setPartyDated(partyBooking.getPartyDated());
//                Venue venue = partyBooking.getThemeInVenue().getVenue();
////                venue.setSlotInRoomList(null);
//                partyBooking.setVenue(venue);
//
//                float pricingUpgradeService = 0;
//                for (UpgradeService upgradeService : partyBooking.getUpgradeServices()) {
//                    pricingUpgradeService += upgradeService.getServices().getPricing() * upgradeService.getCount();
//                }
//
//                partyBooking.setPricingTotal(partyBooking.getPackageInVenue().getApackage().getPricing() + pricingUpgradeService);
//            }
//            if (partyBookingList.isEmpty()) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "List is empty", null));
//            }
//            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", partyBookingList));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
//        }
//    }

    @Override
    public ResponseEntity<ResponseObj> getAll() {
        try {
            List<PartyBooking> partyBookingList = partyBookingRepository.findAllByIsActiveIsTrue();
            if (partyBookingList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "List is empty", null));
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", partyBookingList));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> getAllCompleted() {
        try {
            List<PartyBooking> partyBookingList = partyBookingRepository.findAllByIsActiveIsTrueAndStatus(StatusEnum.COMPLETED);
            if (partyBookingList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "List is empty", null));
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", partyBookingList));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> getAllForHost() {
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

//    @Override
//    public ResponseEntity<ResponseObj> getById_ForCustomer(Long id) {
//        try {
//            Long userId = AuthenUtil.getCurrentUserId();
//            if(userId == null){
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "400", null));
//            }
//            Optional<PartyBooking> partyBooking = partyBookingRepository.findById(id);
//            if (partyBooking.isPresent() && partyBooking.get().isActive()) {
//                if (!partyBooking.get().getAccount().getId().equals(userId)){
//                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseObj(HttpStatus.FORBIDDEN.toString(), "User not permission to see this party", null));
//                }
//                PartyBooking partyBooking1 = partyBooking.get();
//                partyBooking1.setSlotInRoomObject(partyBooking1.getPartyDated().getSlotInRoom());
//                Venue venue = partyBooking1.getThemeInVenue().getVenue();
////                venue.setSlotInRoomList(null);
//                partyBooking1.setVenue(venue);
//                float pricingUpgradeService = 0;
//                for (UpgradeService upgradeService : partyBooking1.getUpgradeServices()) {
//                    pricingUpgradeService += upgradeService.getServices().getPricing() * upgradeService.getCount();
//                }
//                for (Payment payment: partyBooking.get().getPaymentList()){
//                    if(payment.getStatus().equals("SUCCESS")){
//                        partyBooking1.setIsPayment(true);
//                    } else {
//                        partyBooking1.setIsPayment(false);
//                    }
//                }
//
//                partyBooking1.setPricingTotal(partyBooking1.getPackageInVenue().getApackage().getPricing() + pricingUpgradeService);
//                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", partyBooking1));
//            }
//
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This party booking does not exist", null));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
//        }
//    }

//    @Override
//    public ResponseEntity<ResponseObj> create(PartyBookingRequest partyBookingRequest) {
//        try {
//            Long userId = AuthenUtil.getCurrentUserId();
//            if (userId == null) {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "400", null));
//            }
//            Account account = accountRepository.findById(userId).get();
//
//            Optional<ThemeInVenue> themeInVenue = themeInVenueRepository.findById(partyBookingRequest.getThemeInVenueId());
//            Optional<PackageInVenue> packageInVenue = packageInVenueRepository.findById(partyBookingRequest.getPackageInVenueId());
//            Optional<SlotInRoom> slotInVenue = slotInRoomRepository.findById(partyBookingRequest.getSlotInVenueId());
//            if (slotInVenue.isEmpty()) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Slot in venue does not exist", null));
//            }
//            if (themeInVenue.isEmpty()) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Theme in venue does not exist", null));
//            }
//            if (packageInVenue.isEmpty()) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Package in venue does not exist", null));
//            }
//
//            PartyBooking partyBooking = new PartyBooking();
//            partyBooking.setKidName(partyBookingRequest.getKidName());
//            partyBooking.setKidDOB(partyBookingRequest.getKidDOB());
//            partyBooking.setEmail(partyBookingRequest.getEmail());
//            partyBooking.setPhone(partyBookingRequest.getPhone());
//            partyBooking.setStatus(StatusEnum.PENDING);
//            partyBooking.setActive(true);
//            partyBooking.setCreateAt(LocalDateTime.now());
//            partyBooking.setUpdateAt(LocalDateTime.now());
//            partyBooking.setAccount(account);
//            partyBooking.setThemeInVenue(themeInVenue.get());
//            partyBooking.setPackageInVenue(packageInVenue.get());
//            //PartyDated
//            PartyDated partyDate = new PartyDated();
//            partyDate.setDate(partyBookingRequest.getDate());
//            partyDate.setSlotInRoom(slotInVenue.get());
//            partyDate.setActive(true);
//            partyDate.setCreateAt(LocalDateTime.now());
//            partyDate.setUpdateAt(LocalDateTime.now());
//            partyDatedRepository.save(partyDate);
//
//            partyBooking.setPartyDated(partyDate);
//            partyBookingRepository.save(partyBooking);
//
//            List<UpgradeServiceRequest> dataUpgrade = partyBookingRequest.getDataUpgrade();
//            for (UpgradeServiceRequest data : dataUpgrade) {
//                UpgradeService upgradeService = new UpgradeService();
//                Optional<Services> optionalService = servicesRepository.findById(data.getServiceId());
//                if (optionalService.isPresent()) {
//                    Services service = optionalService.get();
//                    upgradeService.setPricing(data.getCount() * service.getPricing());
//                    upgradeService.setServices(service);
//                } else {
//                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Service does not exist", null));
//                }
//                upgradeService.setCount(data.getCount());
//                upgradeService.setActive(true);
//                upgradeService.setCreateAt(LocalDateTime.now());
//                upgradeService.setUpdateAt(LocalDateTime.now());
//                upgradeService.setPartyBooking(partyBooking);
//                upgradeServiceRepository.save(upgradeService);
//            }
//            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Create successful", partyBooking));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
//        }
//    }

//    @Override
//    public ResponseEntity<ResponseObj> update(Long id, PartyBookingRequest partyBookingRequest) {
//        try {
//            Long userId = AuthenUtil.getCurrentUserId();
//            if (userId == null) {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "400", null));
//            }
//
//            Account account = accountRepository.findById(userId).get();
//            Optional<PartyBooking> existPartyBooking = partyBookingRepository.findById(id);
//            if (existPartyBooking.isPresent()) {
//                if (!existPartyBooking.get().getAccount().getId().equals(account.getId())) {
//                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseObj(HttpStatus.FORBIDDEN.toString(), "User not permission to update this booking", null));
//                }
//
//                Optional<PartyDated> existPartyDated = partyDatedRepository.findPartyDatedByPartyBookingId(id);
//                if (partyBookingRequest.getDate() != null || partyBookingRequest.getSlotInVenueId() != null) {
//                    Optional<SlotInRoom> optionalSlotInVenue = slotInRoomRepository.findById(partyBookingRequest.getSlotInVenueId());
//                    if (optionalSlotInVenue.isPresent()) {
//                        SlotInRoom slotInRoom = optionalSlotInVenue.get();
//                        existPartyDated.get().setSlotInRoom(slotInRoom);
//                    } else {
//                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Slot in venue does not exist", null));
//                    }
//                    existPartyDated.get().setDate(partyBookingRequest.getDate());
//                    existPartyDated.get().setActive(true);
//                    existPartyDated.get().setCreateAt(LocalDateTime.now());
//                    existPartyDated.get().setUpdateAt(LocalDateTime.now());
//                    partyDatedRepository.save(existPartyDated.get());
//                }
//
//                if (partyBookingRequest.getThemeInVenueId() != null) {
//                    Optional<ThemeInVenue> optionalThemeInVenue = themeInVenueRepository.findById(partyBookingRequest.getThemeInVenueId());
//                    if (optionalThemeInVenue.isPresent()) {
//                        ThemeInVenue themeInVenue = optionalThemeInVenue.get();
//                        existPartyBooking.get().setThemeInVenue(themeInVenue);
//                    } else {
//                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Theme in venue does not exist", null));
//                    }
//                } else {
//                    existPartyBooking.get().setThemeInVenue(existPartyBooking.get().getThemeInVenue());
//                }
//
//                if (partyBookingRequest.getPackageInVenueId() != null) {
//                    Optional<PackageInVenue> optionalPackageInVenue = packageInVenueRepository.findById(partyBookingRequest.getPackageInVenueId());
//                    if (optionalPackageInVenue.isPresent()) {
//                        PackageInVenue packageInVenue = optionalPackageInVenue.get();
//                        existPartyBooking.get().setPackageInVenue(packageInVenue);
//                    } else {
//                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Package in venue does not exist", null));
//                    }
//                } else {
//                    existPartyBooking.get().setPackageInVenue(existPartyBooking.get().getPackageInVenue());
//                }
//
//                existPartyBooking.get().setKidName(partyBookingRequest.getKidName() == null ? existPartyBooking.get().getKidName() : partyBookingRequest.getKidName());
//                existPartyBooking.get().setKidDOB(partyBookingRequest.getKidDOB() == null ? existPartyBooking.get().getKidDOB() : partyBookingRequest.getKidDOB());
//                existPartyBooking.get().setEmail(partyBookingRequest.getEmail() == null ? existPartyBooking.get().getEmail() : partyBookingRequest.getEmail());
//                existPartyBooking.get().setPhone(partyBookingRequest.getPhone() == null ? existPartyBooking.get().getPhone() : partyBookingRequest.getPhone());
//                existPartyBooking.get().setUpdateAt(LocalDateTime.now());
//
//                partyBookingRepository.save(existPartyBooking.get());
//
//                List<UpgradeServiceRequest> dataUpgrade = partyBookingRequest.getDataUpgrade();
//                if (dataUpgrade != null) {
//                    for (UpgradeServiceRequest data : dataUpgrade) {
//                        Long serviceId = data.getServiceId();
//                        int count = data.getCount();
//
//                        Optional<Services> services = servicesRepository.findById(serviceId);
//                        if (services.isEmpty()) {
//                            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This service does not exist", null));
//                        }
//
//                        Optional<UpgradeService> existingUpgradeService = upgradeServiceRepository.findByPartyBookingAndServices(existPartyBooking.get(), services.get());
//                        if (existingUpgradeService.isPresent()) {
//                            UpgradeService upgradeService = existingUpgradeService.get();
//                            upgradeService.setCount(count);
//                            upgradeService.setPricing(count * services.get().getPricing());
//                            upgradeService.setActive(true);
//                            upgradeService.setCreateAt(LocalDateTime.now());
//                            upgradeService.setUpdateAt(LocalDateTime.now());
//                            upgradeService.setPartyBooking(existPartyBooking.get());
//                            upgradeService.setServices(services.get());
//                            upgradeServiceRepository.save(upgradeService);
//                        } else {
//                            UpgradeService newUpgradeService = new UpgradeService();
//                            newUpgradeService.setCount(count);
//                            newUpgradeService.setPricing(count * services.get().getPricing());
//                            newUpgradeService.setActive(true);
//                            newUpgradeService.setCreateAt(LocalDateTime.now());
//                            newUpgradeService.setUpdateAt(LocalDateTime.now());
//                            newUpgradeService.setPartyBooking(existPartyBooking.get());
//                            newUpgradeService.setServices(services.get());
//                            upgradeServiceRepository.save(newUpgradeService);
//                        }
//                    }
//                }
//                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Update successful", existPartyBooking));
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
//        }
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "User does not exist", null));
//    }

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

//    @Override
//    public ResponseEntity<ResponseObj> updateThemeInVenue(Long partyBookingId, Long themeInVenueId) {
//        try {
//            Optional<PartyBooking> partyBookingOptional = partyBookingRepository.findById(partyBookingId);
//            if (partyBookingOptional.isPresent()) {
//                PartyBooking partyBooking = partyBookingOptional.get();
//
//                Optional<ThemeInVenue> themeInVenueOptional = themeInVenueRepository.findById(themeInVenueId);
//                if (themeInVenueOptional.isPresent()) {
//                    ThemeInVenue newThemeInVenue = themeInVenueOptional.get();
//                    partyBooking.setThemeInVenue(newThemeInVenue);
//                    partyBookingRepository.save(partyBooking);
//
//                    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Update successful", null));
//                } else {
//                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Theme in venue not found", null));
//                }
//            } else {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Party booking not found", null));
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
//        }
//    }

    @Override
    public ResponseEntity<ResponseObj> Cancel(Long bookingId) {
        Long userId = AuthenUtil.getCurrentUserId();
        if(userId == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "400", null));
        }
        Account account = accountRepository.findById(userId).get();
        Optional<PartyBooking> partyBooking = partyBookingRepository.findById(bookingId);
        if(partyBooking.isPresent()){
            if(!partyBooking.get().getAccount().getId().equals(account.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseObj(HttpStatus.FORBIDDEN.toString(), "User not permission to cancel this party booking", null));
            }else{
                partyBooking.get().setStatus(StatusEnum.CANCELLED);
                partyBooking.get().setDeleteAt(LocalDateTime.now());
                partyBooking.get().setActive(false);
                partyBooking.get().getPartyDated().setActive(false);
                partyBookingRepository.save(partyBooking.get());
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(),"Cancel successfully", partyBooking));
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Cancel failed", null));
    }
    @Override
    @Transactional
    public ResponseEntity<ResponseObj> cancelBookingForHost(Long bookingId) {
        try {
            Optional<PartyBooking> partyBooking = partyBookingRepository.findById(bookingId);
            if (partyBooking.isPresent()) {
                if (partyBooking.get().getStatus() == StatusEnum.PENDING || partyBooking.get().getStatus() == StatusEnum.CONFIRMED) {
                    partyBooking.get().setStatus(StatusEnum.CANCELLED);
                    partyBooking.get().setDeleteAt(LocalDateTime.now());
                    partyBooking.get().setActive(false);
                    partyBooking.get().getPartyDated().setActive(false);
                    partyBookingRepository.save(partyBooking.get());

                    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Cancel successful", null));
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Cannot cancel booking with current status", null));
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Party booking not found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }
    @Override
    @Transactional
    public ResponseEntity<ResponseObj> completeBookingForHost(Long bookingId) {
            Optional<PartyBooking> partyBooking = partyBookingRepository.findById(bookingId);
            if (partyBooking.isPresent()) {
                LocalTime currentTime = LocalTime.now();
                Time timeStart = Time.valueOf(partyBooking.get().getPartyDated().getSlotInRoom().getSlot().getTimeStart());
                LocalTime localTimeStart = timeStart.toLocalTime();
                if (partyBooking.get().getStatus() == StatusEnum.CONFIRMED && currentTime.isAfter(localTimeStart.plusHours(1))) {
                    partyBooking.get().setStatus(StatusEnum.COMPLETED);
                    partyBooking.get().setUpdateAt(LocalDateTime.now());
                    partyBookingRepository.save(partyBooking.get());

                    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Party booking completed successfully", null));
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Cannot complete booking at this time", null));
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Party booking not found", null));
            }
    }


    @Override
    @Transactional
    public ResponseEntity<ResponseObj> cancelBookingForCustomer(Long bookingId) {
        try {
            Long userId = AuthenUtil.getCurrentUserId();
            if(userId == null){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "400", null));
            }
            Account account = accountRepository.findById(userId).get();
            Optional<PartyBooking> partyBooking = partyBookingRepository.findById(bookingId);
            if (partyBooking.isPresent()) {
                if (partyBooking.get().getAccount().getId().equals(account.getId())) {
                    if (partyBooking.get().getStatus() == StatusEnum.PENDING) {
                        partyBooking.get().setStatus(StatusEnum.CANCELLED);
                        partyBooking.get().setDeleteAt(LocalDateTime.now());
                        partyBooking.get().setActive(false);
                        partyBooking.get().getPartyDated().setActive(false);
                        partyBookingRepository.save(partyBooking.get());

                        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Cancel successful", null));
                    }
                    else {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Cannot cancel booking with current status", null));
                    }
                }
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseObj(HttpStatus.FORBIDDEN.toString(), "User not permission to cancel this party", null));

            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Party booking not found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public List<PartyBooking> findConfirmedBookings() {
        return partyBookingRepository.findAllByStatus(StatusEnum.CONFIRMED);
    }

    @Override
    public void updateCronJob(Long bookingId, PartyBooking partyBooking){
        Optional<PartyBooking> partyBookingOptional = partyBookingRepository.findById(bookingId);
        partyBookingOptional.get().setStatus(partyBooking.getStatus());
        partyBookingRepository.save(partyBookingOptional.get());
    }

    @Override
    public ResponseEntity<ResponseObj> updatePackageInVenue(Long partyBookingId, Long packageInVenueId) {
        try {
            Optional<PartyBooking> partyBookingOptional = partyBookingRepository.findById(partyBookingId);
            if (partyBookingOptional.isPresent()) {
                PartyBooking partyBooking = partyBookingOptional.get();

                Optional<PackageInVenue> packageInVenue = packageInVenueRepository.findById(packageInVenueId);
                if (packageInVenue.isPresent()) {
                    PackageInVenue newPackageInVenue = packageInVenue.get();
//                    partyBooking.setPackageInVenue(newPackageInVenue);
                    partyBookingRepository.save(partyBooking);

                    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Update successful", null));
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Package in venue not found", null));
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Party booking not found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }
}

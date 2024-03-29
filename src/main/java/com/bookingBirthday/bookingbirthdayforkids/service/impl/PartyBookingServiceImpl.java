package com.bookingBirthday.bookingbirthdayforkids.service.impl;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.PartyBookingRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.request.UpgradeServiceRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.model.*;
import com.bookingBirthday.bookingbirthdayforkids.repository.*;
import com.bookingBirthday.bookingbirthdayforkids.service.PartyBookingService;
import com.bookingBirthday.bookingbirthdayforkids.util.AuthenUtil;
import com.bookingBirthday.bookingbirthdayforkids.util.TotalPriceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PartyBookingServiceImpl implements PartyBookingService {

    @Autowired
    PartyBookingRepository partyBookingRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    UpgradeServiceRepository upgradeServiceRepository;
    @Autowired
    ServicesRepository servicesRepository;
    @Autowired
    SlotInRoomRepository slotInRoomRepository;
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    PackageInBookingRepository packageInBookingRepository;

    //Sửa
    @Override
    public ResponseEntity<ResponseObj> getAllByUser() {
        try {
            Long userId = AuthenUtil.getCurrentUserId();
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "400", null));
            }
            List<PartyBooking> partyBookingList = partyBookingRepository.findAllByIsActiveIsTrueAndAccountId(userId);
            for (PartyBooking partyBooking : partyBookingList) {
                partyBooking.setVenueObject(partyBooking.getSlotInRoom().getRoom().getVenue());

                float pricing = 0;
                for (UpgradeService upgradeService : partyBooking.getUpgradeServices()) {
                    pricing += upgradeService.getServices().getPricing() * upgradeService.getCount();
                }

                pricing += TotalPriceUtil.getTotalPricingPackage(partyBooking);

                partyBooking.setPricingTotal(pricing);

                for (Payment payment : partyBooking.getPaymentList()) {
                    if (payment.getStatus().equals("SUCCESS")) {
                        partyBooking.setIsPayment(true);
                    } else {
                        partyBooking.setIsPayment(false);
                    }
                }
            }
            if (partyBookingList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "List is empty", null));
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", partyBookingList));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> getAll_ForHost(Long venueId) {
        try {
            List<PartyBooking> partyBookingList = partyBookingRepository.findAll();
            for (PartyBooking partyBooking : partyBookingList) {
                partyBooking.setVenueObject(partyBooking.getSlotInRoom().getRoom().getVenue());

                float pricing = 0;
                for (UpgradeService upgradeService : partyBooking.getUpgradeServices()) {
                    pricing += upgradeService.getServices().getPricing() * upgradeService.getCount();
                }

                pricing += TotalPriceUtil.getTotalPricingPackage(partyBooking);

                partyBooking.setPricingTotal(pricing);

                for (Payment payment : partyBooking.getPaymentList()) {
                    if (payment.getStatus().equals("SUCCESS")) {
                        partyBooking.setIsPayment(true);
                    } else {
                        partyBooking.setIsPayment(false);
                    }
                }
            }
            if (partyBookingList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "List is empty", null));
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", partyBookingList));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> getAllCompleted(Long VenueId) {
        try {
            List<PartyBooking> partyBookingList = partyBookingRepository.findAllByIsActiveIsTrueAndStatus(StatusEnum.COMPLETED);
            for (PartyBooking partyBooking : partyBookingList) {
                partyBooking.setVenueObject(partyBooking.getSlotInRoom().getRoom().getVenue());

                float pricing = 0;
                for (UpgradeService upgradeService : partyBooking.getUpgradeServices()) {
                    pricing += upgradeService.getServices().getPricing() * upgradeService.getCount();
                }

                pricing += TotalPriceUtil.getTotalPricingPackage(partyBooking);

                partyBooking.setPricingTotal(pricing);

                for (Payment payment : partyBooking.getPaymentList()) {
                    if (payment.getStatus().equals("SUCCESS")) {
                        partyBooking.setIsPayment(true);
                    } else {
                        partyBooking.setIsPayment(false);
                    }
                }
            }
            if (partyBookingList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "List is empty", null));
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", partyBookingList));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> getById_ForHost(Long id, Long VenueId) {
        try {
            Optional<PartyBooking> partyBooking = partyBookingRepository.findById(id);
            if (partyBooking.isPresent()) {
                partyBooking.get().setVenueObject(partyBooking.get().getSlotInRoom().getRoom().getVenue());

                float pricing = 0;
                for (UpgradeService upgradeService : partyBooking.get().getUpgradeServices()) {
                    pricing += upgradeService.getServices().getPricing() * upgradeService.getCount();
                }

                pricing += TotalPriceUtil.getTotalPricingPackage(partyBooking.get());

                partyBooking.get().setPricingTotal(pricing);

                for (Payment payment : partyBooking.get().getPaymentList()) {
                    if (payment.getStatus().equals("SUCCESS")) {
                        partyBooking.get().setIsPayment(true);
                    } else {
                        partyBooking.get().setIsPayment(false);
                    }
                }
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", partyBooking));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This party booking does not exist", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> getById_ForCustomer(Long id) {
        try {
            Long userId = AuthenUtil.getCurrentUserId();
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "400", null));
            }
            Optional<PartyBooking> partyBooking = partyBookingRepository.findById(id);
            if (partyBooking.isPresent() && partyBooking.get().isActive()) {
                if (!partyBooking.get().getAccount().getId().equals(userId)) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseObj(HttpStatus.FORBIDDEN.toString(), "User not permission to see this party", null));
                }
                partyBooking.get().setVenueObject(partyBooking.get().getSlotInRoom().getRoom().getVenue());

                float pricing = 0;
                for (UpgradeService upgradeService : partyBooking.get().getUpgradeServices()) {
                    pricing += upgradeService.getServices().getPricing() * upgradeService.getCount();
                }

                pricing += TotalPriceUtil.getTotalPricingPackage(partyBooking.get());

                partyBooking.get().setPricingTotal(pricing);

                for (Payment payment : partyBooking.get().getPaymentList()) {
                    if (payment.getStatus().equals("SUCCESS")) {
                        partyBooking.get().setIsPayment(true);
                    } else {
                        partyBooking.get().setIsPayment(false);
                    }
                }
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", partyBooking));
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This party booking does not exist", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    //Sửa
    @Override
    public ResponseEntity<ResponseObj> create(PartyBookingRequest partyBookingRequest) {
        try {
            Long userId = AuthenUtil.getCurrentUserId();
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "400", null));
            }
            Account account = accountRepository.findById(userId).get();

            Optional<PackageInVenue> packageInVenueDeco = packageInVenueRepository.findById(partyBookingRequest.getPackageInVenueDecoId());
            Optional<PackageInVenue> packageInVenueFood = packageInVenueRepository.findById(partyBookingRequest.getPackageInVenueFoodId());
            Optional<SlotInRoom> slotInRoom = slotInRoomRepository.findById(partyBookingRequest.getSlotInRoomId());
            if (slotInRoom.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Slot in room does not exist", null));
            }
            if (packageInVenueDeco.isEmpty() || packageInVenueFood.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Package in venue does not exist", null));
            }

            PartyBooking partyBooking = new PartyBooking();
            partyBooking.setKidName(partyBookingRequest.getKidName());
            partyBooking.setKidDOB(partyBookingRequest.getKidDOB());
            partyBooking.setEmail(partyBookingRequest.getEmail());
            partyBooking.setPhone(partyBookingRequest.getPhone());
            partyBooking.setStatus(StatusEnum.PENDING);
            partyBooking.setActive(true);
            partyBooking.setParticipantAmount(partyBookingRequest.getParticipantAmount());
            partyBooking.setCreateAt(LocalDateTime.now());
            partyBooking.setUpdateAt(LocalDateTime.now());
            partyBooking.setAccount(account);
            partyBookingRepository.save(partyBooking);

            //PartyDated
            PartyDated partyDate = new PartyDated();
            partyDate.setDate(partyBookingRequest.getDate());
            partyDate.setSlotInRoom(slotInRoom.get());
            partyDate.setActive(true);
            partyDate.setCreateAt(LocalDateTime.now());
            partyDate.setUpdateAt(LocalDateTime.now());
            partyDatedRepository.save(partyDate);

            partyBooking.setPartyDated(partyDate);
            partyBookingRepository.save(partyBooking);

            //UpgradeService
            List<UpgradeServiceRequest> dataUpgrade = partyBookingRequest.getDataUpgrade();
            for (UpgradeServiceRequest data : dataUpgrade) {
                UpgradeService upgradeService = new UpgradeService();
                Optional<Services> optionalService = servicesRepository.findById(data.getServiceId());
                if (optionalService.isPresent()) {
                    upgradeService.setPricing(data.getCount() * optionalService.get().getPricing());
                    upgradeService.setServices(optionalService.get());
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

            //Package
            Optional<PackageInVenue> packageDeco = packageInVenueRepository.findById(partyBookingRequest.getPackageInVenueDecoId());
            if (packageDeco.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Package in venue does not exist", null));
            }
            Optional<PackageInVenue> packageFood = packageInVenueRepository.findById(partyBookingRequest.getPackageInVenueFoodId());
            if (packageFood.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Package in venue does not exist", null));
            }
            PackageInBooking packageInBookingDeco = new PackageInBooking();
            packageInBookingDeco.setPackageInVenue(packageDeco.get());
            packageInBookingDeco.setActive(true);
            packageInBookingDeco.setCreateAt(LocalDateTime.now());
            packageInBookingDeco.setUpdateAt(LocalDateTime.now());
            packageInBookingDeco.setPartyBooking(partyBooking);
            packageInBookingRepository.save(packageInBookingDeco);

            PackageInBooking packageInBookingFood = new PackageInBooking();
            packageInBookingFood.setPackageInVenue(packageFood.get());
            packageInBookingFood.setActive(true);
            packageInBookingFood.setCreateAt(LocalDateTime.now());
            packageInBookingFood.setUpdateAt(LocalDateTime.now());
            packageInBookingFood.setPartyBooking(partyBooking);
            packageInBookingRepository.save(packageInBookingFood);

            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Create successful", partyBooking));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> updateUpgradeService(Long id, PartyBookingRequest partyBookingRequest) {
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

                // Upgrade
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
    public ResponseEntity<ResponseObj> updateDate(Long id, PartyBookingRequest partyBookingRequest) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseObj> updatePackage(Long id, PartyBookingRequest partyBookingRequest) {
        try {
            Long userId = AuthenUtil.getCurrentUserId();
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "400", null));
            }

            Account account = accountRepository.findById(userId).get();
            Optional<PartyBooking> existPartyBooking = partyBookingRepository.findById(id);
//            if (existPartyBooking.isPresent()) {
//                if (!existPartyBooking.get().getAccount().getId().equals(account.getId())) {
//                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseObj(HttpStatus.FORBIDDEN.toString(), "User not permission to update this booking", null));
//                }
//
//                // PackageDeco
//                if (partyBookingRequest.getPackageInVenueDecoId() != null && partyBookingRequest.getPackageInVenueFoodId() != null) {
//                    List<PackageInBooking> optionalPackageInBookingDeco = packageInBookingRepository.findByPartyBookingId(id);
//                    if (optionalPackageInBookingDeco.isPresent() && optionalPackageInBookingFood.isPresent()) {
//                        List<PackageInBooking> packageInVenues = new ArrayList<>();
//                        packageInVenues.add(optionalPackageInBookingDeco.get());
//                        packageInVenues.add(optionalPackageInBookingFood.get());
//                        existPartyBooking.get().getPackageInBookings().get();
//                    } else {
//                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Package in venue does not exist", null));
//                    }
//                } else {
//                    existPartyBooking.get().setPackageInVenue(existPartyBooking.get().getPackageInVenue());
//                }
//                partyBookingRepository.save(existPartyBooking.get());
//
//                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Update successful", existPartyBooking));
//            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "User does not exist", null));
    }

    @Override
    public ResponseEntity<ResponseObj> updateBasicInfo(Long id, PartyBookingRequest partyBookingRequest) {
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

                existPartyBooking.get().setKidName(partyBookingRequest.getKidName() == null ? existPartyBooking.get().getKidName() : partyBookingRequest.getKidName());
                existPartyBooking.get().setKidDOB(partyBookingRequest.getKidDOB() == null ? existPartyBooking.get().getKidDOB() : partyBookingRequest.getKidDOB());
                existPartyBooking.get().setEmail(partyBookingRequest.getEmail() == null ? existPartyBooking.get().getEmail() : partyBookingRequest.getEmail());
                existPartyBooking.get().setPhone(partyBookingRequest.getPhone() == null ? existPartyBooking.get().getPhone() : partyBookingRequest.getPhone());
                existPartyBooking.get().setUpdateAt(LocalDateTime.now());

                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Update successful", existPartyBooking));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "User does not exist", null));
    }


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
//                existPartyBooking.get().setKidName(partyBookingRequest.getKidName() == null ? existPartyBooking.get().getKidName() : partyBookingRequest.getKidName());
//                existPartyBooking.get().setKidDOB(partyBookingRequest.getKidDOB() == null ? existPartyBooking.get().getKidDOB() : partyBookingRequest.getKidDOB());
//                existPartyBooking.get().setEmail(partyBookingRequest.getEmail() == null ? existPartyBooking.get().getEmail() : partyBookingRequest.getEmail());
//                existPartyBooking.get().setPhone(partyBookingRequest.getPhone() == null ? existPartyBooking.get().getPhone() : partyBookingRequest.getPhone());
//                existPartyBooking.get().setUpdateAt(LocalDateTime.now());
//
//                // PartyDated
//                Optional<PartyDated> existPartyDated = partyDatedRepository.findPartyDatedByPartyBookingId(id);
//                if (partyBookingRequest.getDate() != null || partyBookingRequest.getSlotInRoomId() != null) {
//                    Optional<SlotInRoom> optionalSlotInRoom = slotInRoomRepository.findById(partyBookingRequest.getSlotInRoomId());
//                    if (optionalSlotInRoom.isPresent()) {
//                        existPartyDated.get().setSlotInRoom(optionalSlotInRoom.get());
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
//                // PackageDeco
//                if (partyBookingRequest.getPackageInVenueDecoId() != null || partyBookingRequest.getPackageInVenueFoodId() != null) {
//                    Optional<PackageInVenue> optionalPackageInVenue = packageInVenueRepository.findById(partyBookingRequest.getPackageInVenueDecoId());
//                    if (optionalPackageInVenue.isPresent()) {
//                        PackageInVenue packageInVenue = optionalPackageInVenue.get();
//                        existPartyBooking.get().setPackageInBookings(packageInVenue);
//                    } else {
//                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Package in venue does not exist", null));
//                    }
//                } else {
//                    existPartyBooking.get().setPackageInVenue(existPartyBooking.get().getPackageInVenue());
//                }
//                partyBookingRepository.save(existPartyBooking.get());
//                //PackageFood
//                if (partyBookingRequest.getPackageInVenueFoodId() != null) {
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
//                partyBookingRepository.save(existPartyBooking.get());
//
//                // Upgrade
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

    @Override
    public ResponseEntity<ResponseObj> Cancel(Long bookingId) {
        Long userId = AuthenUtil.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "400", null));
        }
        Account account = accountRepository.findById(userId).get();
        Optional<PartyBooking> partyBooking = partyBookingRepository.findById(bookingId);
        if (partyBooking.isPresent()) {
            if (!partyBooking.get().getAccount().getId().equals(account.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseObj(HttpStatus.FORBIDDEN.toString(), "User not permission to cancel this party booking", null));
            } else {
                partyBooking.get().setStatus(StatusEnum.CANCELLED);
                partyBooking.get().setDeleteAt(LocalDateTime.now());
                partyBooking.get().setActive(false);
                partyBooking.get().getPartyDated().setActive(false);
                partyBookingRepository.save(partyBooking.get());
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Cancel successfully", partyBooking));
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
            if (userId == null) {
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
                    } else {
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
    public void updateCronJob(Long bookingId, PartyBooking partyBooking) {
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

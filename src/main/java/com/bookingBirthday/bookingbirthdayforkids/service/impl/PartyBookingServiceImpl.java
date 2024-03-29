package com.bookingBirthday.bookingbirthdayforkids.service.impl;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.PartyBookingRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.request.UpgradeServiceRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.model.*;
import com.bookingBirthday.bookingbirthdayforkids.model.Package;
import com.bookingBirthday.bookingbirthdayforkids.repository.*;
import com.bookingBirthday.bookingbirthdayforkids.service.PartyBookingService;
import com.bookingBirthday.bookingbirthdayforkids.util.AuthenUtil;
import com.bookingBirthday.bookingbirthdayforkids.util.TotalPriceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalDate;
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
    PackageInBookingRepository packageInBookingRepository;
    @Autowired
    SlotRepository slotRepository;
    @Autowired
    PackageRepository packageRepository;

    //Sửa
    @Override
    public ResponseEntity<ResponseObj> getAllByUser() {
        try {
            Long userId = AuthenUtil.getCurrentUserId();
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "User does not exist", null));
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
                    partyBooking.setIsPayment(payment.getStatus().equals("SUCCESS"));
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
    public ResponseEntity<ResponseObj> getAll_ForHost() {
        try {
            Long userId = AuthenUtil.getCurrentUserId();
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "User does not exist", null));
            }
            List<Slot> slotList = slotRepository.findAllByAccountId(userId);

            if (!slotList.isEmpty()) {
                List<PartyBooking> partyBookingList = new ArrayList<>();
                for (Slot slot : slotList) {
                    List<SlotInRoom> slotInRoomList = slot.getSlotInRoom();
                    for (SlotInRoom slotInRoom : slotInRoomList) {
                        List<PartyBooking> partyBookingTemp = slotInRoom.getPartyBookingList();
                        partyBookingList.addAll(partyBookingTemp);
                    }
                }
                if (partyBookingList.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "List is empty", null));
                }
                for (PartyBooking partyBooking : partyBookingList) {
                    partyBooking.setVenueObject(partyBooking.getSlotInRoom().getRoom().getVenue());

                    float pricing = 0;
                    for (UpgradeService upgradeService : partyBooking.getUpgradeServices()) {
                        pricing += upgradeService.getServices().getPricing() * upgradeService.getCount();
                    }

                    pricing += TotalPriceUtil.getTotalPricingPackage(partyBooking);

                    partyBooking.setPricingTotal(pricing);

                    for (Payment payment : partyBooking.getPaymentList()) {
                        partyBooking.setIsPayment(payment.getStatus().equals("SUCCESS"));
                    }
                }
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", partyBookingList));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "List is empty", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> getAllCompleted() {
        try {
            Long userId = AuthenUtil.getCurrentUserId();
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "User does not exist", null));
            }

            List<Slot> slotList = slotRepository.findAllByAccountId(userId);

            if (!slotList.isEmpty()) {
                List<PartyBooking> partyBookingList = new ArrayList<>();
                for (Slot slot : slotList) {
                    List<SlotInRoom> slotInRoomList = slot.getSlotInRoom();
                    for (SlotInRoom slotInRoom : slotInRoomList) {
                        List<PartyBooking> partyBookingTemp = slotInRoom.getPartyBookingList();
                        for (PartyBooking partyBooking : partyBookingTemp) {
                            if (partyBooking.getStatus().equals(StatusEnum.COMPLETED)) {
                                partyBookingList.add(partyBooking);
                            }
                        }
                    }
                }
                if (partyBookingList.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "List is empty", null));
                }
                for (PartyBooking partyBooking : partyBookingList) {
                    partyBooking.setVenueObject(partyBooking.getSlotInRoom().getRoom().getVenue());

                    float pricing = 0;
                    for (UpgradeService upgradeService : partyBooking.getUpgradeServices()) {
                        pricing += upgradeService.getServices().getPricing() * upgradeService.getCount();
                    }

                    pricing += TotalPriceUtil.getTotalPricingPackage(partyBooking);

                    partyBooking.setPricingTotal(pricing);

                    for (Payment payment : partyBooking.getPaymentList()) {
                        partyBooking.setIsPayment(payment.getStatus().equals("SUCCESS"));
                    }
                }
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", partyBookingList));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "List is empty", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> getById_ForHost(Long partyBookingId) {
        try {
            Long userId = AuthenUtil.getCurrentUserId();
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "User does not exist", null));
            }

            Optional<PartyBooking> partyBooking = partyBookingRepository.findById(partyBookingId);

            if (partyBooking.isPresent()) {
                if (partyBooking.get().getSlotInRoom().getSlot().getAccount().getId().equals(userId)) {
                    partyBooking.get().setVenueObject(partyBooking.get().getSlotInRoom().getRoom().getVenue());

                    float pricing = 0;
                    for (UpgradeService upgradeService : partyBooking.get().getUpgradeServices()) {
                        pricing += upgradeService.getServices().getPricing() * upgradeService.getCount();
                    }

                    pricing += TotalPriceUtil.getTotalPricingPackage(partyBooking.get());

                    partyBooking.get().setPricingTotal(pricing);

                    for (Payment payment : partyBooking.get().getPaymentList()) {
                        partyBooking.get().setIsPayment(payment.getStatus().equals("SUCCESS"));
                    }
                    return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", partyBooking.get()));
                }
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseObj(HttpStatus.FORBIDDEN.toString(), "User not permission to impact this booking", null));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Party booking not found", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> getById_ForCustomer(Long partyBookingId) {
        try {
            Long userId = AuthenUtil.getCurrentUserId();
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "User does not exist", null));
            }
            Optional<PartyBooking> partyBooking = partyBookingRepository.findById(partyBookingId);
            if (partyBooking.isPresent() && partyBooking.get().isActive()) {
                if (!partyBooking.get().getAccount().getId().equals(userId)) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseObj(HttpStatus.FORBIDDEN.toString(), "User not permission to see this booking", null));
                }
                partyBooking.get().setVenueObject(partyBooking.get().getSlotInRoom().getRoom().getVenue());

                float pricing = 0;
                for (UpgradeService upgradeService : partyBooking.get().getUpgradeServices()) {
                    pricing += upgradeService.getServices().getPricing() * upgradeService.getCount();
                }

                pricing += TotalPriceUtil.getTotalPricingPackage(partyBooking.get());

                partyBooking.get().setPricingTotal(pricing);

                for (Payment payment : partyBooking.get().getPaymentList()) {
                    partyBooking.get().setIsPayment(payment.getStatus().equals("SUCCESS"));
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
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "User does not exist", null));
            }
            Optional<Account> account = accountRepository.findById(userId);
            if (account.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Account does not exist", null));
            }

            Optional<Package> packageDeco = packageRepository.findById(partyBookingRequest.getPackageDecoId());
            Optional<Package> packageFood = packageRepository.findById(partyBookingRequest.getPackageFoodId());
            Optional<SlotInRoom> slotInRoom = slotInRoomRepository.findById(partyBookingRequest.getSlotInRoomId());
            if (slotInRoom.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Slot in room does not exist", null));
            }
            if (packageDeco.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Package deco does not exist", null));
            }
            if (packageFood.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Package food does not exist", null));
            }

            //PartyBooking
            PartyBooking partyBooking = new PartyBooking();
            partyBooking.setKidName(partyBookingRequest.getKidName());
            partyBooking.setKidDOB(partyBookingRequest.getKidDOB());
            partyBooking.setEmail(partyBookingRequest.getEmail());
            partyBooking.setPhone(partyBookingRequest.getPhone());
            partyBooking.setStatus(StatusEnum.PENDING);
            partyBooking.setDate(partyBookingRequest.getDate());
            partyBooking.setActive(true);
            partyBooking.setParticipantAmount(partyBookingRequest.getParticipantAmount());
            partyBooking.setCreateAt(LocalDateTime.now());
            partyBooking.setUpdateAt(LocalDateTime.now());
            partyBooking.setAccount(account.get());
            partyBooking.setSlotInRoom(slotInRoom.get());
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
            PackageInBooking packageInBookingDeco = new PackageInBooking();
            packageInBookingDeco.setAPackage(packageDeco.get());
            packageInBookingDeco.setActive(true);
            packageInBookingDeco.setCreateAt(LocalDateTime.now());
            packageInBookingDeco.setUpdateAt(LocalDateTime.now());
            packageInBookingDeco.setPartyBooking(partyBooking);
            packageInBookingRepository.save(packageInBookingDeco);

            PackageInBooking packageInBookingFood = new PackageInBooking();
            packageInBookingFood.setAPackage(packageFood.get());
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
    public ResponseEntity<ResponseObj> updateUpgradeService(Long partyBookingId, PartyBookingRequest partyBookingRequest) {
        try {
            Long userId = AuthenUtil.getCurrentUserId();
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "User does not exist", null));
            }

            Optional<PartyBooking> existPartyBooking = partyBookingRepository.findById(partyBookingId);
            if (existPartyBooking.isPresent()) {
                if (!existPartyBooking.get().getAccount().getId().equals(userId)) {
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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This party booking does not exist", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> updateOrganizationTime(Long partyBookingId, LocalDate date, Long slotInRoomId) {
        try {
            Long userId = AuthenUtil.getCurrentUserId();
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "User does not exist", null));
            }

            Optional<PartyBooking> existPartyBooking = partyBookingRepository.findByIdAndIsActiveIsTrue(partyBookingId);
            if (existPartyBooking.isPresent()) {
                if (!existPartyBooking.get().getAccount().getId().equals(userId)) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseObj(HttpStatus.FORBIDDEN.toString(), "User not permission to update this booking", null));
                }

                Optional<SlotInRoom> optionalSlotInRoom = slotInRoomRepository.findById(slotInRoomId);
                if (optionalSlotInRoom.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Slot in room does not exist", null));
                }

                if (!existPartyBooking.get().getSlotInRoom().getRoom().getVenue().getId().equals(optionalSlotInRoom.get().getRoom().getVenue().getId())) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Slot in room does not exist in this Venue", null));
                }

                if (date != null) {
                    existPartyBooking.get().setSlotInRoom(optionalSlotInRoom.get());
                    existPartyBooking.get().setDate(date);
                    existPartyBooking.get().setUpdateAt(LocalDateTime.now());
                    partyBookingRepository.save(existPartyBooking.get());
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Date is invalid", null));
                }
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Update successful", existPartyBooking));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This party booking does not exist", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> updatePackage(Long partyBookingId, PartyBookingRequest partyBookingRequest) {
        try {
            Long userId = AuthenUtil.getCurrentUserId();
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "User does not exist", null));
            }

            Optional<PartyBooking> existPartyBooking = partyBookingRepository.findByIdAndIsActiveIsTrue(partyBookingId);
            if (existPartyBooking.isPresent()) {
                if (!existPartyBooking.get().getAccount().getId().equals(userId)) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseObj(HttpStatus.FORBIDDEN.toString(), "User not permission to update this booking", null));
                }

                List<PackageInBooking> packageInBookingList = existPartyBooking.get().getPackageInBookings();
                for (PackageInBooking packageInBooking : packageInBookingList) {
                    Optional<Package> packageDeco = packageRepository.findById(partyBookingRequest.getPackageDecoId());
                    Optional<Package> packageFood = packageRepository.findById(partyBookingRequest.getPackageFoodId());
                    if (packageDeco.isEmpty()) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Package deco does not exist", null));
                    }
                    if (packageFood.isEmpty()) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Package food does not exist", null));
                    }

                    if (packageInBooking.getAPackage().getPackageType().equals(TypeEnum.DECORATION)) {
                        if (!packageInBooking.getAPackage().getId().equals(partyBookingRequest.getPackageDecoId())) {
                            packageInBooking.setAPackage(packageDeco.get());
                            packageInBooking.setUpdateAt(LocalDateTime.now());
                            packageInBookingRepository.save(packageInBooking);
                        }
                    }
                    if (packageInBooking.getAPackage().getPackageType().equals(TypeEnum.FOOD)) {
                        if (!packageInBooking.getAPackage().getId().equals(partyBookingRequest.getPackageFoodId())) {
                            packageInBooking.setAPackage(packageFood.get());
                            packageInBooking.setUpdateAt(LocalDateTime.now());
                            packageInBookingRepository.save(packageInBooking);
                        }
                    }
                }
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Update successful", existPartyBooking));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This party booking does not exist", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> updateBasicInfo(Long partyBookingId, PartyBookingRequest partyBookingRequest) {
        try {
            Long userId = AuthenUtil.getCurrentUserId();
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "User does not exist", null));
            }

            Optional<PartyBooking> existPartyBooking = partyBookingRepository.findByIdAndIsActiveIsTrue(partyBookingId);
            if (existPartyBooking.isPresent()) {
                if (!existPartyBooking.get().getAccount().getId().equals(userId)) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseObj(HttpStatus.FORBIDDEN.toString(), "User not permission to update this booking", null));
                }

                existPartyBooking.get().setKidName(partyBookingRequest.getKidName() == null ? existPartyBooking.get().getKidName() : partyBookingRequest.getKidName());
                existPartyBooking.get().setKidDOB(partyBookingRequest.getKidDOB() == null ? existPartyBooking.get().getKidDOB() : partyBookingRequest.getKidDOB());
                existPartyBooking.get().setEmail(partyBookingRequest.getEmail() == null ? existPartyBooking.get().getEmail() : partyBookingRequest.getEmail());
                existPartyBooking.get().setPhone(partyBookingRequest.getPhone() == null ? existPartyBooking.get().getPhone() : partyBookingRequest.getPhone());
                existPartyBooking.get().setParticipantAmount(partyBookingRequest.getParticipantAmount() == 0 ? existPartyBooking.get().getParticipantAmount() : partyBookingRequest.getParticipantAmount());
                existPartyBooking.get().setUpdateAt(LocalDateTime.now());

                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Update successful", existPartyBooking));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This party booking does not exist", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }


    //sửa
    @Override
    public ResponseEntity<ResponseObj> cancelBooking_ForCustomer(Long bookingId) {
        try {
            Long userId = AuthenUtil.getCurrentUserId();
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "User does not exist", null));
            }
            Optional<PartyBooking> partyBooking = partyBookingRepository.findByIdAndIsActiveIsTrue(bookingId);
            if (partyBooking.isPresent()) {
                if (partyBooking.get().getAccount().getId().equals(userId)) {
                    if (partyBooking.get().getStatus() == StatusEnum.PENDING) {
                        partyBooking.get().getPackageInBookings().forEach(packageInBooking -> {
                            packageInBooking.setActive(false);
                            packageInBooking.setDeleteAt(LocalDateTime.now());
                            packageInBookingRepository.save(packageInBooking);
                        });
                        partyBooking.get().getUpgradeServices().forEach(upgradeService -> {
                            upgradeService.setActive(false);
                            upgradeService.setDeleteAt(LocalDateTime.now());
                            upgradeServiceRepository.save(upgradeService);
                        });
                        partyBooking.get().setStatus(StatusEnum.CANCELLED);
                        partyBooking.get().setDeleteAt(LocalDateTime.now());
                        partyBooking.get().setActive(false);
                        partyBookingRepository.save(partyBooking.get());

                        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Cancel successful", null));
                    } else {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Cannot cancel booking with current status", null));
                    }
                }
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseObj(HttpStatus.FORBIDDEN.toString(), "User not permission to cancel this booking", null));

            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Party booking not found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }


    //sửa
    @Override
    public ResponseEntity<ResponseObj> cancelBooking_ForHost(Long bookingId) {
        try {
            Long userId = AuthenUtil.getCurrentUserId();
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "User does not exist", null));
            }
            Optional<PartyBooking> partyBooking = partyBookingRepository.findById(bookingId);
            if (partyBooking.isPresent()) {
                if (partyBooking.get().getSlotInRoom().getSlot().getAccount().getId().equals(userId)) {
                    if (partyBooking.get().getStatus() == StatusEnum.CANCELLED) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "The party booking has already been cancelled", null));
                    }
                    if (partyBooking.get().getStatus() == StatusEnum.PENDING || partyBooking.get().getStatus() == StatusEnum.CONFIRMED) {
                        partyBooking.get().getPackageInBookings().forEach(packageInBooking -> {
                            packageInBooking.setActive(false);
                            packageInBooking.setDeleteAt(LocalDateTime.now());
                            packageInBookingRepository.save(packageInBooking);
                        });
                        partyBooking.get().getUpgradeServices().forEach(upgradeService -> {
                            upgradeService.setActive(false);
                            upgradeService.setDeleteAt(LocalDateTime.now());
                            upgradeServiceRepository.save(upgradeService);
                        });
                        partyBooking.get().setStatus(StatusEnum.CANCELLED);
                        partyBooking.get().setDeleteAt(LocalDateTime.now());
                        partyBooking.get().setActive(false);
                        partyBookingRepository.save(partyBooking.get());

                        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Cancel successful", null));
                    } else {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Cannot cancel booking with current status", null));
                    }
                }
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseObj(HttpStatus.FORBIDDEN.toString(), "User not permission to cancel this booking", null));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Party booking not found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> deleteBooking_ForHost(Long bookingId) {
        try {
            Long userId = AuthenUtil.getCurrentUserId();
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "User does not exist", null));
            }
            Optional<PartyBooking> partyBooking = partyBookingRepository.findById(bookingId);
            if (partyBooking.isPresent()) {
                if (partyBooking.get().getSlotInRoom().getSlot().getAccount().getId().equals(userId)) {
                    partyBooking.get().getPackageInBookings().forEach(packageInBooking -> {
                        packageInBooking.setActive(false);
                        packageInBooking.setDeleteAt(LocalDateTime.now());
                        packageInBookingRepository.save(packageInBooking);
                    });
                    partyBooking.get().getUpgradeServices().forEach(upgradeService -> {
                        upgradeService.setActive(false);
                        upgradeService.setDeleteAt(LocalDateTime.now());
                        upgradeServiceRepository.save(upgradeService);
                    });
                    partyBooking.get().setDeleteAt(LocalDateTime.now());
                    partyBooking.get().setActive(false);
                    partyBookingRepository.save(partyBooking.get());

                    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Delete successful", null));
                }
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseObj(HttpStatus.FORBIDDEN.toString(), "User not permission to cancel this booking", null));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Party booking not found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> completeBooking_ForHost(Long bookingId) {

        try {
            Long userId = AuthenUtil.getCurrentUserId();
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "User does not exist", null));
            }
            Optional<PartyBooking> partyBooking = partyBookingRepository.findById(bookingId);
            if (partyBooking.isPresent()) {
                if (partyBooking.get().getSlotInRoom().getSlot().getAccount().getId().equals(userId)) {
                    if (partyBooking.get().getStatus() == StatusEnum.CANCELLED) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "The party booking has already been cancelled", null));
                    }
                    LocalTime currentTime = LocalTime.now();
                    Time timeStart = Time.valueOf(partyBooking.get().getSlotInRoom().getSlot().getTimeStart());
                    LocalTime localTimeStart = timeStart.toLocalTime();
                    if (partyBooking.get().getStatus() == StatusEnum.CONFIRMED && currentTime.isAfter(localTimeStart.plusHours(1))) {
                        partyBooking.get().setStatus(StatusEnum.COMPLETED);
                        partyBooking.get().setUpdateAt(LocalDateTime.now());
                        partyBookingRepository.save(partyBooking.get());

                        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Party booking completed successfully", null));
                    } else {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Cannot complete booking at this time", null));
                    }
                }
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseObj(HttpStatus.FORBIDDEN.toString(), "User not permission to cancel this booking", null));
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
}

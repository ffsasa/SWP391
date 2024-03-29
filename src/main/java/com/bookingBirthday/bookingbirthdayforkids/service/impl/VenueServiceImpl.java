package com.bookingBirthday.bookingbirthdayforkids.service.impl;

import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.model.*;
import com.bookingBirthday.bookingbirthdayforkids.model.Package;
import com.bookingBirthday.bookingbirthdayforkids.repository.*;
import com.bookingBirthday.bookingbirthdayforkids.service.SlotInRoomService;
import com.bookingBirthday.bookingbirthdayforkids.service.VenueService;
import com.bookingBirthday.bookingbirthdayforkids.util.AuthenUtil;
import com.bookingBirthday.bookingbirthdayforkids.util.TotalPriceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VenueServiceImpl implements VenueService {

    @Autowired
    VenueRepository venueRepository;

    @Autowired
    PackageRepository packageRepository;

    @Autowired
    FirebaseService firebaseService;

    @Autowired
    SlotRepository slotRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    SlotInRoomRepository slotInRoomRepository;

    @Autowired
    AccountRepository accountRepository;

    @Override
    public ResponseEntity<ResponseObj> getAll() {
        try {
            List<Venue> venueList = venueRepository.findAllByIsActiveIsTrue();
            if (venueList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "List is empty", null));
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", venueList));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> getAllForHost() {
        try {
            List<Venue> venueList = venueRepository.findAll();
            if (venueList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "List is empty", null));
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", venueList));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }


    @Override
    public ResponseEntity<ResponseObj> getAllPartyBookingByVenue(Long venueId) {
        try {
            Optional<Venue> venue = venueRepository.findById(venueId);
            if (venue.isPresent()) {
                List<Room> roomList = venue.get().getRoomList();
                List<PartyBooking> partyBookingList = new ArrayList<>();
                for (Room room : roomList) {
                    List<SlotInRoom> slotInRoomList = room.getSlotInRoomList();
                    for (SlotInRoom slotInRoom : slotInRoomList) {
                        List<PartyBooking> partyBookings = slotInRoom.getPartyBookingList();
                        partyBookingList.addAll(partyBookings);
                    }
                }
                if (partyBookingList.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This venue doest not have any booking", null));
                } else {
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
                    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Ok", partyBookingList));
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This venue does not exist", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }


    @Override
    public ResponseEntity<ResponseObj> getById_ForCustomer(Long id) {
        try {
            Optional<Venue> venue = venueRepository.findById(id);
            if (venue.isPresent() && venue.get().isActive()) {
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", venue));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This venue does not exist", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    //sửa
    @Override
    public ResponseEntity<ResponseObj> customize(Long id, MultipartFile imgFile, String venueName, String venueDescription, String street, String ward, String district, String city) {
        Long userId = AuthenUtil.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "400", null));
        }
        Account account = accountRepository.findById(userId).get();
        Optional<Venue> venue = venueRepository.findById(id);

        if (venue.isPresent()) {
            if (!venue.get().getAccount().getId().equals(account.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseObj(HttpStatus.FORBIDDEN.toString(), "User not permission to impact this venue", null));
            }
            try {
                if (imgFile != null) {
                    String img = firebaseService.uploadImage(imgFile);
                    if (venueRepository.existsByVenueName(venueName)) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Venue name has already exist", null));
                    }
                    venue.get().setVenueName(venueName);
                    venue.get().setVenueDescription(venueDescription);
                    venue.get().setVenueImgUrl(img);
                    venue.get().setStreet(street);
                    venue.get().setDistrict(district);
                    venue.get().setWard(ward);
                    venue.get().setCity(city);
                    venue.get().setActive(true);
                    venue.get().setCreateAt(LocalDateTime.now());
                    venueRepository.save(venue.get());
                    return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Create successful", venue));
                }
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Image is invalid", null));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This venue does not exist", null));
    }

    //sửa
    public ResponseEntity<ResponseObj> activeVenue(Long id) {
        Optional<Venue> venue = venueRepository.findById(id);
        if (!venue.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This venue does not exist", null));
        }
        try {
            List<Room> roomList = venue.get().getRoomList();
            for (Room room : roomList) {
                List<SlotInRoom> slotInRoomList = room.getSlotInRoomList();
                for (SlotInRoom slotInRoom : slotInRoomList) {
                    slotInRoom.setUpdateAt(LocalDateTime.now());
                    slotInRoom.setActive(true);
                }
                room.setUpdateAt(LocalDateTime.now());
                room.setActive(true);
            }

            venue.get().setActive(true);
            venueRepository.save(venue.get());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Set venue active successful", venue));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }


    //sửa
    @Override
    public ResponseEntity<ResponseObj> update(Long id, MultipartFile imgFile, String venueName, String venueDescription, String street, String ward, String district, String city) {
        Long userId = AuthenUtil.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "400", null));
        }
        Account account = accountRepository.findById(userId).get();
        Optional<Venue> venue = venueRepository.findById(id);
        if (venue.isPresent()) {
            if (!venue.get().getAccount().getId().equals(account.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseObj(HttpStatus.FORBIDDEN.toString(), "You are not permission update this venue.", null));
            }
            try {
                venue.get().setVenueName((venueName == null || venue.get().getVenueName().equals(venueName)) ? venue.get().getVenueName() : venueName);
                venue.get().setVenueDescription((venueDescription == null || venue.get().getVenueDescription().equals(venueDescription)) ? venue.get().getVenueDescription() : venueDescription);
                venue.get().setVenueImgUrl(imgFile == null ? venue.get().getVenueImgUrl() : firebaseService.uploadImage(imgFile));
                venue.get().setStreet((street == null || venue.get().getStreet().equals(street)) ? venue.get().getStreet() : street);
                venue.get().setWard((ward == null || venue.get().getWard().equals(ward)) ? venue.get().getWard() : ward);
                venue.get().setDistrict((district == null || venue.get().getDistrict().equals(district)) ? venue.get().getDistrict() : district);
                venue.get().setCity((city == null || venue.get().getCity().equals(city)) ? venue.get().getCity() : city);
                venue.get().setUpdateAt(LocalDateTime.now());
                venueRepository.save(venue.get());
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Update successful", venue));

            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Venue does not exist", venue));
    }

    //sửa

    @Override
    public ResponseEntity<ResponseObj> delete(Long id) {
        try {
            Optional<Venue> venue = venueRepository.findById(id);
            if (venue.isPresent()) {
                List<Room> roomList = venue.get().getRoomList();
                for (Room room : roomList) {
                    List<SlotInRoom> slotInRoomList = room.getSlotInRoomList();
                    for (SlotInRoom slotInRoom : slotInRoomList) {
                        slotInRoom.setDeleteAt(LocalDateTime.now());
                        slotInRoom.setActive(false);
                        slotInRoomRepository.save(slotInRoom);
                    }
                    room.setDeleteAt(LocalDateTime.now());
                    room.setActive(false);
                    roomRepository.save(room);
                }
                List<Package> packageList = venue.get().getPackageList();
                for (Package aPackage : packageList){
                    List<PackageService> packageServiceList = aPackage.getPackageServiceList();
                    for (PackageService packageService : packageServiceList){
                        packageService.setActive(false);
                        packageService.setDeleteAt(LocalDateTime.now());
                    }
                    aPackage.setActive(false);
                    aPackage.setDeleteAt(LocalDateTime.now());
                }
                venue.get().setDeleteAt(LocalDateTime.now());
                venue.get().setActive(false);
                venueRepository.save(venue.get());
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Delete successful", null));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This venue does not exist", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

}

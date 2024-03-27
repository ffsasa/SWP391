package com.bookingBirthday.bookingbirthdayforkids.service.impl;

import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.model.*;
import com.bookingBirthday.bookingbirthdayforkids.model.Package;
import com.bookingBirthday.bookingbirthdayforkids.repository.*;
import com.bookingBirthday.bookingbirthdayforkids.service.SlotInRoomService;
import com.bookingBirthday.bookingbirthdayforkids.service.VenueService;
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
    PartyDatedRepository partyDatedRepository;

    @Autowired
    FirebaseService firebaseService;

    @Autowired
    PackageInVenueRepository packageInVenueRepository;

    @Autowired
    SlotRepository slotRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    SlotInRoomRepository slotInRoomRepository;

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
    public ResponseEntity<ResponseObj> getPackageInVenueByVenue(Long venueId) {
        try {
            Optional<Venue> venue = venueRepository.findById(venueId);
            if (venue.isPresent()) {
                List<PackageInVenue> packageInVenuesList = venue.get().getPackageInVenueList();
                List<PackageInVenue> packageInVenuesListValidate = new ArrayList<>();
                for (PackageInVenue packageInVenue : packageInVenuesList) {
                    if (packageInVenue.isActive()) {
                        packageInVenuesListValidate.add(packageInVenue);
                    }
                }
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", packageInVenuesListValidate));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "This venue does not exist", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> getAllPackageHaveNotAddByVenune(Long venueId) {
        try {
            Optional<Venue> venue = venueRepository.findById(venueId);
            if (venue.isPresent()) {
                List<PackageInVenue> packageInVenueList = venue.get().getPackageInVenueList();
                List<Package> packageAddedList = new ArrayList<>();
                for (PackageInVenue packageInVenue : packageInVenueList) {
                    packageAddedList.add(packageInVenue.getApackage());
                }
                List<Package> packageList = packageRepository.findAll();
                List<Package> packageNotAddList = new ArrayList<>();
                for (Package apacakge : packageList) {
                    if (!packageAddedList.contains(apacakge)) {
                        packageNotAddList.add(apacakge);
                    }
                }
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", packageNotAddList));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "This theme does not exist", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> getById(Long id) {
        try {
            Optional<Venue> venue = venueRepository.findById(id);
            if (venue.isPresent()) {
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", venue));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This venue does not exist", null));
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

    @Override
    public ResponseEntity<ResponseObj> create(MultipartFile imgFile, String venueName, String venueDescription, String street, String ward, String district, String city) {
        if (venueRepository.existsByVenueName(venueName)) {
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(new ResponseObj(HttpStatus.ALREADY_REPORTED.toString(), "Venue name has already exist", null));
        }
        Venue venue = new Venue();
        try {
            if (imgFile != null) {
                String img = firebaseService.uploadImage(imgFile);
                venue.setVenueName(venueName);
                venue.setVenueDescription(venueDescription);
                venue.setVenueImgUrl(img);
                venue.setStreet(street);
                venue.setDistrict(district);
                venue.setWard(ward);
                venue.setCity(city);
                venue.setActive(false);
                venue.setCreateAt(LocalDateTime.now());
                venueRepository.save(venue);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Image is invalid", null));
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Create successful", venue));
    }

    public ResponseEntity<ResponseObj> activeVenue(Long id) {
        Optional<Venue> venue = venueRepository.findById(id);
        if (!venue.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This venue does not exist", null));
        }
        try {
            List<Room> roomList = venue.get().getRoomList();
            for(Room room : roomList){
                List<SlotInRoom> slotInRoomList = room.getSlotInRoomList();
                for(SlotInRoom slotInRoom : slotInRoomList){
                    slotInRoom.setUpdateAt(LocalDateTime.now());
                    slotInRoom.setActive(true);
                }
                room.setUpdateAt(LocalDateTime.now());
                room.setActive(true);
            }

            List<PackageInVenue> packageInVenueList = venue.get().getPackageInVenueList();
            for(PackageInVenue packageInVenue : packageInVenueList){
                packageInVenue.setUpdateAt(LocalDateTime.now());
                packageInVenue.setActive(true);
            }
            venue.get().setActive(true);
            venueRepository.save(venue.get());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Set venue active successful", venue));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }


    @Override
    public ResponseEntity<ResponseObj> update(Long id, MultipartFile imgFile, String venueName, String venueDescription, String street, String ward, String district, String city) {
        Optional<Venue> venue = venueRepository.findById(id);
        if (!venue.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This restaurant does not exist", null));
        }
        try {

            venue.get().setVenueName(venueName == null ? venue.get().getVenueName() : venueName);
            venue.get().setVenueDescription(venueDescription == null ? venue.get().getVenueDescription() : venueDescription);
            venue.get().setVenueImgUrl(imgFile == null ? venue.get().getVenueImgUrl() : firebaseService.uploadImage(imgFile));
            venue.get().setStreet(street == null ? venue.get().getStreet() : street);
            venue.get().setWard(ward == null ? venue.get().getWard() : ward);
            venue.get().setDistrict(district == null ? venue.get().getDistrict() : district);
            venue.get().setCity(city == null ? venue.get().getCity() : city);
            venue.get().setUpdateAt(LocalDateTime.now());
            venueRepository.save(venue.get());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Update successful", venue));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
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

                List<PackageInVenue> packageInVenueList = venue.get().getPackageInVenueList();
                for (PackageInVenue packageInVenue : packageInVenueList) {
                    packageInVenue.setDeleteAt(LocalDateTime.now());
                    packageInVenue.setActive(false);
                    packageInVenueRepository.save(packageInVenue);
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



    @Override
    public ResponseEntity<ResponseObj> addPackage(Long venueId, Long packageId) {
        try{
            Optional<Package> aPackage = packageRepository.findById(packageId);
            Optional<Venue> venue = venueRepository.findById(venueId);
            if(!aPackage.isPresent()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This package does not exist", null));
            }
            if(venue.isPresent()){
                PackageInVenue packageInVenue = new PackageInVenue();
                packageInVenue.setApackage(aPackage.get());
                packageInVenue.setVenue(venue.get());
                packageInVenue.setActive(true);
                packageInVenue.setCreateAt(LocalDateTime.now());
                packageInVenue.setUpdateAt(LocalDateTime.now());
                packageInVenueRepository.save(packageInVenue);
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Add package in venue successfully", null));
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This venue does not exist", null));
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

}

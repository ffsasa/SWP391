package com.bookingBirthday.bookingbirthdayforkids.service.impl;

import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.model.*;
import com.bookingBirthday.bookingbirthdayforkids.model.Package;
import com.bookingBirthday.bookingbirthdayforkids.repository.PackageRepository;
import com.bookingBirthday.bookingbirthdayforkids.repository.PartyDatedRepository;
import com.bookingBirthday.bookingbirthdayforkids.repository.ThemeRepository;
import com.bookingBirthday.bookingbirthdayforkids.repository.VenueRepository;
import com.bookingBirthday.bookingbirthdayforkids.service.VenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class VenueServiceImpl implements VenueService {

    @Autowired
    VenueRepository venueRepository;

    @Autowired
    PackageRepository packageRepository;

    @Autowired
    ThemeRepository themeRepository;

    @Autowired
    PartyDatedRepository partyDatedRepository;

    @Autowired
    FirebaseService firebaseService;

    @Override
    public ResponseEntity<ResponseObj> getAll() {
        try {
            List<Venue> venueList = venueRepository.findAllByIsActiveIsTrue();
            if(venueList.isEmpty()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "List is empty", null));
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", venueList));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> checkSlotInVenue(LocalDate date) {
        try {
            List<Venue> venueList = venueRepository.findAllByIsActiveIsTrue();
            if(venueList.isEmpty()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "List is empty", null));
            }
            List<PartyDated> partyDatedList = partyDatedRepository.findAllByDate(date);

            for(Venue venue : venueList){
                List<SlotInVenue> slotInVenueList = venue.getSlotInVenueList();
                for (SlotInVenue slotInVenue : slotInVenueList) {
                    slotInVenue.setSlotObject(slotInVenue.getSlot());
                    for (PartyDated partyDated : partyDatedList) {
                        if (partyDated.getSlotInVenue().equals(slotInVenue)) {
                            slotInVenue.setStatus(true);
                        }
                    }
                }
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", venueList));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }


    @Override
    public ResponseEntity<ResponseObj> getById(Long id) {
        try {
            Optional<Venue> venue = venueRepository.findById(id);
            if(venue.isPresent()){
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", venue));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This venue does not exist", null));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> create(MultipartFile imgFile, String venueName, String venueDescription, String location, int capacity) {
        if(venueRepository.existsByVenueName(venueName)){
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(new ResponseObj(HttpStatus.ALREADY_REPORTED.toString(),"Venue name has already exist", null));
        }
        Venue venue = new Venue();
        try {
            if (imgFile != null) {
                String img = firebaseService.uploadImage(imgFile);
                venue.setVenueName(venueName);
                venue.setVenueDescription(venueDescription);
                venue.setVenueImgUrl(img);
                venue.setLocation(location);
                venue.setCapacity(capacity);
                venue.setActive(true);
                venue.setCreateAt(LocalDateTime.now());
                venue.setUpdateAt(LocalDateTime.now());
                venueRepository.save(venue);
            }

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Image is invalid", null));

        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Create successful", venue));
    }


    @Override
    public ResponseEntity<ResponseObj> update(Long id, MultipartFile imgFile, String venueName, String venueDescription, String location, int capacity) {
        Optional<Venue> venue = venueRepository.findById(id);
        if (!venue.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This venue does not exist", null));
        }
        try {

            venue.get().setVenueName(venueName == null ? venue.get().getVenueName() : venueName);
            venue.get().setVenueDescription(venueDescription == null ? venue.get().getVenueDescription() : venueDescription);
            venue.get().setLocation(location == null ? venue.get().getLocation() : location);
            venue.get().setCapacity(capacity == 0 ? venue.get().getCapacity() : capacity);
            String img = firebaseService.uploadImage(imgFile);
            venue.get().setVenueImgUrl(img == null ? venue.get().getVenueImgUrl() : img);
            venue.get().setUpdateAt(LocalDateTime.now());
            venueRepository.save(venue.get());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Update successful", venue));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> delete(Long id)
    {
        try {
            Optional<Venue> venue = venueRepository.findById(id);
            if (venue.isPresent()){
                venue.get().setActive(false);
                venue.get().setDeleteAt(LocalDateTime.now());
                venueRepository.save(venue.get());
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Delete successful", null));
            } else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This venue does not exist", null));

        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> addTheme(Long venueId, Long themeId) {
        try {
            Theme theme = themeRepository.findById(themeId).get();
            Optional<Venue> venue = venueRepository.findById(venueId);
            if(venue.isPresent()){
                Set<Theme> themeSet = venueRepository.findById(venueId).get().getThemeSet();
                themeSet.add(theme);
                venue.get().setThemeSet(themeSet);
                venueRepository.save(venue.get());
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Update successful", null));
            } else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This venue does not exist", null));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> addPackage(Long venueId, Long packageId) {
        try {
            Package aPackage = packageRepository.findById(packageId).get();
            Optional<Venue> venue = venueRepository.findById(venueId);
            if (venue.isPresent()) {
                Set<Package> packageSet = venueRepository.findById(venueId).get().getPackageSet();
                packageSet.add(aPackage);
                venue.get().setPackageSet(packageSet);
                venueRepository.save(venue.get());
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Update successful", null));
            } else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This venue does not exist", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }
}

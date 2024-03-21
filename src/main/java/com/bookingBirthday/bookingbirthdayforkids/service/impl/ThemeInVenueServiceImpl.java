package com.bookingBirthday.bookingbirthdayforkids.service.impl;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.PackageInVenueRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.request.ThemeInVenueRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.model.*;
import com.bookingBirthday.bookingbirthdayforkids.model.Package;
import com.bookingBirthday.bookingbirthdayforkids.repository.ThemeInVenueRepository;
import com.bookingBirthday.bookingbirthdayforkids.repository.ThemeRepository;
import com.bookingBirthday.bookingbirthdayforkids.repository.VenueRepository;
import com.bookingBirthday.bookingbirthdayforkids.service.ThemeInVenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ThemeInVenueServiceImpl implements ThemeInVenueService {
    @Autowired
    VenueRepository venueRepository;

    @Autowired
    ThemeRepository themeRepository;

    @Autowired
    ThemeInVenueRepository themeInVenueRepository;

    @Override
    public ResponseEntity<ResponseObj> getAll() {
        try {
            List<ThemeInVenue> themeInVenueList = themeInVenueRepository.findAllByIsActiveIsTrue();
            if (themeInVenueList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "List is empty", null));
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", themeInVenueList));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> getAll_ForHost(){
        try {
            List<ThemeInVenue> themeInVenueList = themeInVenueRepository.findAll();
            if (themeInVenueList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "List is empty", null));
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", themeInVenueList));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

//    @Override
//    public  ResponseEntity<ResponseObj> getById_ForCustomer(Long id){
//        try {
//            Optional<ThemeInVenue> themeInVenue = themeInVenueRepository.findById(id);
//            if (themeInVenue.isPresent() && themeInVenue.get().isActive() == true) {
//                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", themeInVenue));
//            } else {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This theme in venue does not exist", null));
//            }
//        }catch (Exception e){
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
//        }
//    }

    @Override
    public ResponseEntity<ResponseObj> getById(Long id) {
        try {
            Optional<ThemeInVenue> themeInVenue = themeInVenueRepository.findById(id);;
            if(themeInVenue.isPresent())
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), null, themeInVenue));
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Theme In Venue does not exist", null));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> update(Long id, ThemeInVenueRequest themeInVenueRequest) {
        Theme theme = themeRepository.findById(themeInVenueRequest.getThemeId()).get();
        Venue venue = venueRepository.findById(themeInVenueRequest.getVenueId()).get();
        Optional<ThemeInVenue> existThemeVenue = themeInVenueRepository.findById(id);
        if (existThemeVenue.isPresent()){
            existThemeVenue.get().setTheme(theme == null ? existThemeVenue.get().getTheme() : theme);
            existThemeVenue.get().setVenue(venue == null ? existThemeVenue.get().getVenue()  : venue);
            existThemeVenue.get().setUpdateAt(LocalDateTime.now());
            themeInVenueRepository.save(existThemeVenue.get());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Update successful", existThemeVenue));
        }
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Theme In Venue does not exist", null));

    }

    public ResponseEntity<ResponseObj> activeThemeInVenue(Long id){
        try{
            Optional <ThemeInVenue> themeInVenue = themeInVenueRepository.findById(id);
            if(themeInVenue.isPresent()){
                themeInVenue.get().setActive(true);
                themeInVenueRepository.save(themeInVenue.get());
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Enable theme in venue successfully", themeInVenue));
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This theme in venue does not exist", null));
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> delete(Long id) {
        Optional<ThemeInVenue> themeInVenue = themeInVenueRepository.findById(id);
        if (themeInVenue.isPresent()){
            themeInVenue.get().setActive(false);
            themeInVenue.get().setDeleteAt(LocalDateTime.now());
            themeInVenueRepository.save(themeInVenue.get());
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Disable theme in venue successful", null));
        }
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Theme In Venue does not exist", null));
    }
}

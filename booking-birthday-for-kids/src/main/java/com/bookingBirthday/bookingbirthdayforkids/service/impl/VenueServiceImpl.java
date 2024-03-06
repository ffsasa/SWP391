package com.bookingBirthday.bookingbirthdayforkids.service.impl;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.VenueRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.model.Theme;
import com.bookingBirthday.bookingbirthdayforkids.model.Venue;
import com.bookingBirthday.bookingbirthdayforkids.repository.VenueRepository;
import com.bookingBirthday.bookingbirthdayforkids.service.VenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VenueServiceImpl implements VenueService {

    @Autowired
    VenueRepository venueRepository;

    @Override
    public ResponseEntity<ResponseObj> getAll() {
        try {
            List<Venue> venueList = venueRepository.findAll();
            if(venueList.isEmpty()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "List is empty", null));
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
    public ResponseEntity<ResponseObj> create(VenueRequest venueRequest) {
        try {
            if (venueRepository.existsByVenueName(venueRequest.getVenueName()))
                return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(new ResponseObj(HttpStatus.ALREADY_REPORTED.toString(), "This venue name already exists", null));

            Venue venue = new Venue();
            venue.setVenueName(venueRequest.getVenueName());
            venue.setVenueDescription(venueRequest.getVenueDescription());
            venue.setVenueImgUrl(venueRequest.getVenueImgUrl());
            venue.setLocation(venueRequest.getLocation());
            venue.setCapacity(venueRequest.getCapacity());
            venue.setActive(true);
            venue.setCreateAt(LocalDateTime.now());
            venue.setUpdateAt(LocalDateTime.now());

            venueRepository.save(venue);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Create successful", venue));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> update(Long id, VenueRequest venueRequest) {
        try {
            Optional<Venue> existVenue = venueRepository.findById(id);
            if (existVenue.isPresent()){
                existVenue.get().setVenueName(venueRequest.getVenueName() == null ? existVenue.get().getVenueName() : venueRequest.getVenueName());
                existVenue.get().setVenueDescription(venueRequest.getVenueDescription() == null ? existVenue.get().getVenueDescription() : venueRequest.getVenueDescription());
                existVenue.get().setVenueImgUrl(venueRequest.getVenueImgUrl() == null ? existVenue.get().getVenueImgUrl() : venueRequest.getVenueImgUrl());
                existVenue.get().setLocation(venueRequest.getLocation() == null ? existVenue.get().getLocation() : venueRequest.getLocation());
                existVenue.get().setCapacity(venueRequest.getCapacity() == 0 ? existVenue.get().getCapacity() : venueRequest.getCapacity());
                existVenue.get().setUpdateAt(LocalDateTime.now());
                venueRepository.save(existVenue.get());
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Update successful", existVenue));
            }
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This venue does not exist", null));

        }catch (Exception e){
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
}

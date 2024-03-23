package com.bookingBirthday.bookingbirthdayforkids.service.impl;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.PackageInVenueRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.model.*;
import com.bookingBirthday.bookingbirthdayforkids.model.Package;
import com.bookingBirthday.bookingbirthdayforkids.repository.PackageInVenueRepository;
import com.bookingBirthday.bookingbirthdayforkids.repository.PackageRepository;
import com.bookingBirthday.bookingbirthdayforkids.repository.VenueRepository;
import com.bookingBirthday.bookingbirthdayforkids.service.PackageInVenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PackageInVenueServiceImpl implements PackageInVenueService {
    @Autowired
    PackageInVenueRepository packageInVenueRepository;

    @Autowired
    PackageRepository packageRepository;

    @Autowired
    VenueRepository venueRepository;

    @Override
    public ResponseEntity<ResponseObj> getAll() {
        try {
            List<PackageInVenue> packageInVenueList = packageInVenueRepository.findAllByIsActiveIsTrue();
            if (packageInVenueList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "List is empty", null));
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", packageInVenueList));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> getAllForHost(){
        try {
            List<PackageInVenue> packageInVenueList = packageInVenueRepository.findAll();
            if (packageInVenueList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "List is empty", null));
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", packageInVenueList));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }
    @Override
    public ResponseEntity<ResponseObj> getPackageInVenueNotChoose(Long packageInVenueId) {
        try{
            Optional<PackageInVenue> packageInVenueOptional = packageInVenueRepository.findById(packageInVenueId);;

            if(packageInVenueOptional.isPresent()){
                PackageInVenue packageInVenue = packageInVenueOptional.get();
                Venue venue = packageInVenue.getVenue();
                List<PackageInVenue> packageInVenueList = venue.getPackageInVenueList();
                List<PackageInVenue> filteredPackageInVenueList = new ArrayList<>();
                for(PackageInVenue packageInVenue1 : packageInVenueList){
                    if(!Objects.equals(packageInVenue1.getId(), packageInVenueId)){
                        filteredPackageInVenueList.add(packageInVenue1);
                    }
                }
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), null, filteredPackageInVenueList));
            }
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Theme In Venue does not exist", null));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));

        }
    }
    @Override
    public ResponseEntity<ResponseObj> getById(Long id) {
        try {
            Optional<PackageInVenue> packageInVenue = packageInVenueRepository.findById(id);
            if(packageInVenue.isPresent())
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), null, packageInVenue));
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Package In Venue does not exist", null));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public  ResponseEntity<ResponseObj> getById_ForCustomer(Long id){
        try {
            Optional<PackageInVenue> packageInVenue = packageInVenueRepository.findById(id);
            if(packageInVenue.isPresent() && packageInVenue.get().isActive() == true)
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), null, packageInVenue));
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Package In Venue does not exist", null));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }
    @Override
    public ResponseEntity<ResponseObj> activePackageInVenue(Long id){
        try{
            Optional <PackageInVenue> packageInVenue = packageInVenueRepository.findById(id);
            if(packageInVenue.isPresent()){
                packageInVenue.get().setActive(true);
                packageInVenueRepository.save(packageInVenue.get());
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Enable package in venue successfully", packageInVenue));
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This package in venue does not exist", null));
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> update(Long id, PackageInVenueRequest packageInVenueRequest) {
        Package aPackage = packageRepository.findById(packageInVenueRequest.getPackageId()).get();
        Venue venue = venueRepository.findById(packageInVenueRequest.getVenueId()).get();
        Optional<PackageInVenue> existPackageInVenue  = packageInVenueRepository.findById(id);
        if (existPackageInVenue.isPresent()){
            existPackageInVenue.get().setApackage(aPackage == null ? existPackageInVenue.get().getApackage() : aPackage);
            existPackageInVenue.get().setVenue(venue == null ? existPackageInVenue.get().getVenue()  : venue);
            existPackageInVenue.get().setUpdateAt(LocalDateTime.now());
            packageInVenueRepository.save(existPackageInVenue.get());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Update successful", existPackageInVenue));
        }
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Package In Venue does not exist", null));

    }

    @Override
    public ResponseEntity<ResponseObj> delete(Long id) {
        Optional<PackageInVenue> packageInVenue = packageInVenueRepository.findById(id);
        if (packageInVenue.isPresent()){
            packageInVenue.get().setActive(false);
            packageInVenue.get().setDeleteAt(LocalDateTime.now());
            packageInVenueRepository.save(packageInVenue.get());
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Delete successful", null));
        }
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Package In Venue does not exist", null));
    }
}

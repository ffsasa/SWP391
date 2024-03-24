package com.bookingBirthday.bookingbirthdayforkids.service.impl;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.PackageServiceRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.model.*;
import com.bookingBirthday.bookingbirthdayforkids.model.Package;
import com.bookingBirthday.bookingbirthdayforkids.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PackageServiceImpl implements com.bookingBirthday.bookingbirthdayforkids.service.PackageService {
    @Autowired
    PackageRepository packageRepository;
    @Autowired
    PackageServiceRepository packageServiceRepository;
    @Autowired
    ServicesRepository servicesRepository;
    @Autowired
    FirebaseService firebaseService;
    @Autowired
    VenueRepository venueRepository;
    @Autowired
    PackageInVenueRepository packageInVenueRepository;
    @Override
    public ResponseEntity<ResponseObj> getAll() {
        List<Package> packageList = packageRepository.findAllByIsActiveIsTrue();
        if (packageList.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "List is empty", null));

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.OK.toString(), "OK", packageList));
    }
    @Override
    public ResponseEntity<ResponseObj> getAllForHost() {
        try {
            List<Package> packageList = packageRepository.findAll();
            if (packageList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "List is empty", null));
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", packageList));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> getById(Long id) {
        try {
            Optional<Package> Package = packageRepository.findById(id);
            if(Package.isPresent())
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), null, Package));
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Package does not exist", null));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }
    @Override
    public ResponseEntity<ResponseObj> getById_ForCustomer(Long id) {
        try {
            Optional<Package> Package = packageRepository.findById(id);
                if (Package.isPresent() && Package.get().isActive()) {
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", Package));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This package does not exist", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> create(MultipartFile imgFile, String packageName, String packageDescription, float percent,List<PackageServiceRequest> packageServiceRequestList) {
        if (packageRepository.existsByPackageName(packageName)) {
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(new ResponseObj(HttpStatus.ALREADY_REPORTED.toString(), "Package name has already exist", null));
        }
        Package pack = new Package();
        float packPricing = 0;
        try {
            if (imgFile != null) {
                String img = firebaseService.uploadImage(imgFile);
                pack.setPackageName(packageName);
                pack.setPackageImgUrl(img);
                pack.setPackageDescription(packageDescription);
                pack.setActive(true);
                pack.setCreateAt(LocalDateTime.now());
                pack.setUpdateAt(LocalDateTime.now());
                packageRepository.save(pack);
            }
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Image is invalid", null));
        }
        List<PackageServiceRequest> packageServiceRequests = packageServiceRequestList;
        for (PackageServiceRequest packageServiceRequest : packageServiceRequests) {
                PackageService packageService = new PackageService();
                packageService.setCount(packageServiceRequest.getCount());
                packageService.setPricing((packageServiceRequest.getCount()*servicesRepository.findById(packageServiceRequest.getServiceId()).get().getPricing()));
                packageService.setActive(true);
                packageService.setCreateAt(LocalDateTime.now());
                packageService.setUpdateAt(LocalDateTime.now());
                packPricing += packageService.getPricing();
                packageService.setApackage(pack);
                packageService.setServices(servicesRepository.findById(packageServiceRequest.getServiceId()).get());
                packageServiceRepository.save(packageService);
            }
        float newPricing = packPricing * percent;
        pack.setPricing(packPricing - newPricing);
        packageRepository.save(pack);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Successful", pack));
    }



    @Override
    public ResponseEntity<ResponseObj> update(Long id, MultipartFile imgFile, String packageName, String packageDescription) {
        Optional<Package> aPackage = packageRepository.findById(id);
        if (!aPackage.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This package does not exist", null));
        }
        try {

            aPackage.get().setPackageName(packageName == null ? aPackage.get().getPackageName() : packageName);
            aPackage.get().setPackageDescription(packageDescription == null ? aPackage.get().getPackageDescription() : packageDescription);
            aPackage.get().setPackageImgUrl(imgFile == null ? aPackage.get().getPackageImgUrl() : firebaseService.uploadImage(imgFile));
            aPackage.get().setPricing(aPackage.get().getPricing());
            aPackage.get().setUpdateAt(LocalDateTime.now());
            packageRepository.save(aPackage.get());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Update successful", aPackage));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> updatePercentPackage(Long id, float percent) {
        Optional<Package> aPackage = packageRepository.findById(id);
        if (aPackage.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This package does not exist", null));
        }
        float packPricing = 0;

        List<PackageService> packageServiceList = aPackage.get().getPackageServiceList();
        for (PackageService packageService : packageServiceList) {
           packPricing += packageService.getPricing();
        }
        try {
            float newPricing = packPricing * percent;
            aPackage.get().setPricing(packPricing - newPricing);
            aPackage.get().setUpdateAt(LocalDateTime.now());
            packageRepository.save(aPackage.get());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Update successful", aPackage));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> delete(Long id) {
        Optional<Package> pack = packageRepository.findById(id);
        if (pack.isPresent()){
            pack.get().getPackageInVenueList().forEach(packageInVenue -> {packageInVenue.setDeleteAt(LocalDateTime.now());
                packageInVenue.setActive(false);
                packageInVenueRepository.save(packageInVenue);});

            pack.get().getPackageServiceList().forEach(packageService -> {packageService.setDeleteAt(LocalDateTime.now());
                packageService.setActive(false);
                packageServiceRepository.save(packageService);});

            pack.get().setActive(false);
            pack.get().setDeleteAt(LocalDateTime.now());
            packageRepository.save(pack.get());
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Delete successful", null));
        }
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Package does not exist", null));
    }

    @Override
    public ResponseEntity<ResponseObj> addPackageInVenueByVenueId(Long venueId, List<Long> packageIdList){
        Venue venue = venueRepository.findById(venueId).get();
        PackageInVenue packageInVenue = new PackageInVenue();
        ResponseEntity<ResponseObj> response = null;
        for (Long addPackage : packageIdList){
            Package aPackage = packageRepository.findById(addPackage.longValue()).orElse(null);
            if (addPackage == null) {
                response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Package not found", null));
                continue;
            }
            PackageInVenue existingPackageInVenue = packageInVenueRepository.findByVenueAndApackage(venue, aPackage);
            if (existingPackageInVenue != null) {
                response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Package in venue already exists", null));
                continue;
            }
            packageInVenue = new PackageInVenue();
            packageInVenue.setVenue(venue);
            packageInVenue.setApackage(aPackage);
            packageInVenue.setActive(true);
            packageInVenue.setCreateAt(LocalDateTime.now());
            packageInVenue.setUpdateAt(LocalDateTime.now());
            packageInVenueRepository.save(packageInVenue);
        }
        if(response == null){
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Create successful", packageInVenue));
        }
        return response;
    }
}


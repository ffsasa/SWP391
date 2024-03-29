package com.bookingBirthday.bookingbirthdayforkids.service.impl;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.PackageServiceRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.model.Package;
import com.bookingBirthday.bookingbirthdayforkids.model.*;
import com.bookingBirthday.bookingbirthdayforkids.repository.*;
import com.bookingBirthday.bookingbirthdayforkids.util.AuthenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
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
    AccountRepository accountRepository;
    @Autowired
    RoleRepository roleRepository;

    //fix
    @Override
    public ResponseEntity<ResponseObj> getAllForCustomer(Long venueId) {
        Long userId = AuthenUtil.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "User not found", null));
        }

        Optional<Account> account = accountRepository.findById(userId);
        if (!account.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "Account not found", null));
        }

        Role role = roleRepository.findByName(RoleEnum.CUSTOMER);
        if (!account.get().getRole().equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseObj(HttpStatus.FORBIDDEN.toString(), "User is not a customer", null));
        }

        Optional<Venue> venue = venueRepository.findById(venueId);
        if (!venue.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Venue not found", null));
        }
        List<Package> packageList = packageRepository.findAllByVenueIdAndIsActiveIsTrue(venueId);
        if (packageList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "No active packages found for this venue", null));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "OK", packageList));
    }

    //fix


    @Override
    public ResponseEntity<ResponseObj> getAllForHost(Long venueId) {
        Long userId = AuthenUtil.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "User not found", null));
        }

        Optional<Account> account = accountRepository.findById(userId);
        if (!account.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "Account not found", null));
        }

        Role role = roleRepository.findByName(RoleEnum.CUSTOMER);
        if (!account.get().getRole().equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseObj(HttpStatus.FORBIDDEN.toString(), "User is not a customer", null));
        }

        Optional<Venue> venue = venueRepository.findById(venueId);
        if (!venue.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Venue not found", null));
        }
        List<Package> packageList = packageRepository.findAllByVenueId();
        if (packageList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "No active packages found for this venue", null));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "OK", packageList));
    }

    public ResponseEntity<ResponseObj> getByIdForCustomer(Long venueId, Long id) {
        Long userId = AuthenUtil.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "User not found", null));
        }

        Optional<Account> account = accountRepository.findById(userId);
        if (!account.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "Account not found", null));
        }

        Role role = roleRepository.findByName(RoleEnum.CUSTOMER);
        if (!account.get().getRole().equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseObj(HttpStatus.FORBIDDEN.toString(), "User is not a customer", null));
        }

        Optional<Venue> venue = venueRepository.findById(venueId);
        if (!venue.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Venue not found", null));
        }

        Optional<Package> apackage = packageRepository.findById(id);
        if (apackage.isPresent() && apackage.get().getVenue().getId().equals(venueId) && apackage.get().isActive()) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", apackage));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This package does not exist or is inactive", null));
        }
    }

//fix

    @Override
    public ResponseEntity<ResponseObj> getByIdForHost(Long venueId, Long id) {
        Long userId = AuthenUtil.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "User not found", null));
        }

        Optional<Account> account = accountRepository.findById(userId);
        if (!account.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "Account not found", null));
        }

        Role role = roleRepository.findByName(RoleEnum.HOST);
        if (!account.get().getRole().equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseObj(HttpStatus.FORBIDDEN.toString(), "User is not a host", null));
        }

        Optional<Venue> venue = venueRepository.findById(venueId);
        if (!venue.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Venue not found", null));
        }
        Optional<Package> apackage = packageRepository.findById(id);
        if (apackage.isPresent() && apackage.get().getVenue().getId().equals(venueId)) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", apackage));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This package does not exist or is inactive", null));
        }
    }

    //fix

    @Override
    public ResponseEntity<ResponseObj> create(Long venueId, MultipartFile imgFile, String packageName, String packageDescription, float percent, List<PackageServiceRequest> packageServiceRequestList, TypeEnum typeEnum) {
        Long userId = AuthenUtil.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "User not found", null));
        }

        Optional<Account> account = accountRepository.findById(userId);
        if (!account.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "Account not found", null));
        }

        Role role = roleRepository.findByName(RoleEnum.HOST);
        if (!account.get().getRole().equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseObj(HttpStatus.FORBIDDEN.toString(), "User is not a Host", null));
        }

        Optional<Venue> venue = venueRepository.findById(venueId);
        if (!venue.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Venue not found", null));
        }

        if (!venue.get().getAccount().getId().equals(account.get().getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "You are not permission", null));
        }
        Package pack = new Package();
        float packPricing = 0;
        try {
            String img = "";
            if (imgFile != null) {
                switch (typeEnum) {
                    case FOOD, DECORATION:
                        img = firebaseService.uploadImage(imgFile);
                        pack.setPackageName(packageName);
                        pack.setPackageImgUrl(img);
                        pack.setPackageDescription(packageDescription);
                        pack.setActive(true);
                        pack.setCreateAt(LocalDateTime.now());
                        pack.setUpdateAt(LocalDateTime.now());
                        pack.setPackageType(typeEnum);
                        pack.setVenue(venue.get());
                        packageRepository.save(pack);
                        break;
                    default:
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Invalid package type", null));
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Image is invalid", null));
        }

        for (PackageServiceRequest packageServiceRequest : packageServiceRequestList) {
            PackageService packageService = new PackageService();
            packageService.setCount(packageServiceRequest.getCount());
            packageService.setPricing((packageServiceRequest.getCount() * servicesRepository.findById(packageServiceRequest.getServiceId()).get().getPricing()));
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


    //Sửa
//    @Override
//    public ResponseEntity<ResponseObj> create(MultipartFile imgFile, String packageName, String packageDescription, float percent, List<PackageServiceRequest> packageServiceRequestList, TypeEnum typeEnum) {
//        if (packageRepository.existsByPackageName(packageName)) {
//            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(new ResponseObj(HttpStatus.ALREADY_REPORTED.toString(), "Package name has already exist", null));
//        }
//        Package pack = new Package();
//        float packPricing = 0;
//        try {
//            String img = "";
//            if (imgFile != null) {
//                switch (typeEnum) {
//                    case FOOD, DECORATION:
//                        img = firebaseService.uploadImage(imgFile);
//                        pack.setPackageName(packageName);
//                        pack.setPackageImgUrl(img);
//                        pack.setPackageDescription(packageDescription);
//                        pack.setActive(true);
//                        pack.setCreateAt(LocalDateTime.now());
//                        pack.setUpdateAt(LocalDateTime.now());
//                        pack.setPackageType(typeEnum);
//                        packageRepository.save(pack);
//                        break;
//                    default:
//                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Invalid package type", null));
//                }
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Image is invalid", null));
//        }
//
//        for (PackageServiceRequest packageServiceRequest : packageServiceRequestList) {
//            PackageService packageService = new PackageService();
//            packageService.setCount(packageServiceRequest.getCount());
//            packageService.setPricing((packageServiceRequest.getCount() * servicesRepository.findById(packageServiceRequest.getServiceId()).get().getPricing()));
//            packageService.setActive(true);
//            packageService.setCreateAt(LocalDateTime.now());
//            packageService.setUpdateAt(LocalDateTime.now());
//            packPricing += packageService.getPricing();
//            packageService.setApackage(pack);
//            packageService.setServices(servicesRepository.findById(packageServiceRequest.getServiceId()).get());
//            packageServiceRepository.save(packageService);
//        }
//        float newPricing = packPricing * percent;
//        pack.setPricing(packPricing - newPricing);
//        packageRepository.save(pack);
//        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Successful", pack));
//    }

    //fix
    @Override
    public ResponseEntity<ResponseObj> update(Long venueId, Long id, MultipartFile imgFile, String packageName, String packageDescription) {
        Long userId = AuthenUtil.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "User not found", null));
        }
        Optional<Account> account = accountRepository.findById(userId);
        if (!account.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "Account not found", null));
        }
        Role role = roleRepository.findByName(RoleEnum.HOST);
        if (!account.get().getRole().equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseObj(HttpStatus.FORBIDDEN.toString(), "User is not a Host", null));
        }
        Optional<Venue> venue = venueRepository.findById(venueId);
        if (!venue.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Venue not found", null));
        }
        if (!venue.get().getAccount().getId().equals(account.get().getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "You are not permission", null));
        }
        Optional<Package> aPackage = packageRepository.findById(id);
        if (!aPackage.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This package does not exist", null));
        }
        try {

            aPackage.get().setPackageName(packageName == null ? aPackage.get().getPackageName() : packageName);
            aPackage.get().setPackageDescription(packageDescription == null ? aPackage.get().getPackageDescription() : packageDescription);
            aPackage.get().setPackageImgUrl(imgFile == null ? aPackage.get().getPackageImgUrl() : firebaseService.uploadImage(imgFile));
            aPackage.get().setPricing(aPackage.get().getPricing());
            aPackage.get().setVenue(venue.get());
            aPackage.get().setUpdateAt(LocalDateTime.now());
            packageRepository.save(aPackage.get());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Update successful", aPackage));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    //fix
    @Override
    public ResponseEntity<ResponseObj> updatePercentPackage(Long venueId, Long id, float percent) {
        Long userId = AuthenUtil.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "User not found", null));
        }
        Optional<Account> account = accountRepository.findById(userId);
        if (!account.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "Account not found", null));
        }
        Role role = roleRepository.findByName(RoleEnum.HOST);
        if (!account.get().getRole().equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseObj(HttpStatus.FORBIDDEN.toString(), "User is not a Host", null));
        }
        Optional<Venue> venue = venueRepository.findById(venueId);
        if (!venue.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Venue not found", null));
        }
        if (!venue.get().getAccount().getId().equals(account.get().getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "You are not permission", null));
        }
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
            aPackage.get().setVenue(venue.get());
            packageRepository.save(aPackage.get());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Update successful", aPackage));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    //fix
    @Override
    public ResponseEntity<ResponseObj> delete(Long venueId, Long id) {
        Long userId = AuthenUtil.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "User not found", null));
        }
        Optional<Account> account = accountRepository.findById(userId);
        if (!account.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "Account not found", null));
        }
        Role role = roleRepository.findByName(RoleEnum.HOST);
        if (!account.get().getRole().equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseObj(HttpStatus.FORBIDDEN.toString(), "User is not a Host", null));
        }
        Optional<Venue> venue = venueRepository.findById(venueId);
        if (!venue.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Venue not found", null));
        }
        if (!venue.get().getAccount().getId().equals(account.get().getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "You are not permission", null));
        }
        Optional<Package> pack = packageRepository.findById(id);
        if (pack.isPresent()) {
            pack.get().setActive(false);
            pack.get().setDeleteAt(LocalDateTime.now());
            pack.get().getPackageServiceList().forEach(packageService -> {
                packageService.getApackage().setVenue(venue.get());
                packageService.setDeleteAt(LocalDateTime.now());
                packageService.setActive(false);
                packageServiceRepository.save(packageService);
            });
            packageRepository.save(pack.get());
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Delete successful", null));
        } else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Package does not exist", null));
    }
}


package com.bookingBirthday.bookingbirthdayforkids.service.impl;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.PackageRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.model.Package;
import com.bookingBirthday.bookingbirthdayforkids.model.Services;
import com.bookingBirthday.bookingbirthdayforkids.repository.PackageRepository;
import com.bookingBirthday.bookingbirthdayforkids.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PackageServiceImpl implements PackageService {
    @Autowired
    PackageRepository packageRepository;
    @Autowired
    FirebaseService firebaseService;
    @Override
    public ResponseEntity<ResponseObj> getAll() {
        List<Package> packageList = packageRepository.findAllByIsActiveIsTrue();
        if (packageList.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "List is empty", null));

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.OK.toString(), "OK", packageList));
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
    public ResponseEntity<ResponseObj> create(MultipartFile imgFile, String packageName, String packageDescription, float pricing) {
        Package pack = new Package();
        try {
            if (imgFile != null) {
                String img = firebaseService.uploadImage(imgFile);
                pack.setPackageName(packageName);
                pack.setPricing(pricing);
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
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Successful", pack));
    }

    @Override
    public ResponseEntity<ResponseObj> update(Long id, MultipartFile imgFile, String packageName, String packageDescription, float pricing) {
        Optional<Package> aPackage = packageRepository.findById(id);
        if (!aPackage.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This package does not exist", null));
        }
        try {

            aPackage.get().setPackageName(packageName == null ? aPackage.get().getPackageName() : packageName);
            aPackage.get().setPackageDescription(packageDescription == null ? aPackage.get().getPackageDescription() : packageDescription);
            String img = firebaseService.uploadImage(imgFile);
            aPackage.get().setPackageImgUrl(img == null ? aPackage.get().getPackageImgUrl() : img);
            aPackage.get().setPricing(pricing == 0 ? aPackage.get().getPricing() : pricing);
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
            pack.get().setActive(false);
            pack.get().setDeleteAt(LocalDateTime.now());
            packageRepository.save(pack.get());
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Delete successful", null));
        }
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Package does not exist", null));
    }
}


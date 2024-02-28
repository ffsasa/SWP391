package com.bookingBirthday.bookingbirthdayforkids.service.impl;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.PackageRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.model.Account;
import com.bookingBirthday.bookingbirthdayforkids.model.Package;
import com.bookingBirthday.bookingbirthdayforkids.repository.PackageRepository;
import com.bookingBirthday.bookingbirthdayforkids.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PackageServiceImpl implements PackageService {
    @Autowired
    PackageRepository packageRepository;
    @Override
    public ResponseEntity<ResponseObj> getAll() {
        List<Package> packageList = packageRepository.findAll();
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
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Account does not exist", null));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> create(PackageRequest packageRequest) {
        if (packageRepository.existsByPackageName(packageRequest.getPackageName())){
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(new ResponseObj(HttpStatus.ALREADY_REPORTED.toString(), "Package Name has already", null));}
        Package pack = new Package();
        pack.setPackageName(packageRequest.getPackageName());
        pack.setPackageImgUrl(packageRequest.getPackageImgUrl());
        pack.setPricing(packageRequest.getPricing());

        packageRepository.save(pack);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(),"Create successful", pack));
    }

    @Override
    public ResponseEntity<ResponseObj> update(Long id, PackageRequest packageRequest) {
        Optional<Package> existPackage  = packageRepository.findById(id);
        if (existPackage.isPresent()){
            existPackage.get().setUserName(accountRequest.getUserName() == null ? existAccount.get().getUserName() : packageRequest.getUserName());
            existPackage.get().setUpdateAt(LocalDateTime.now());
            packageRepository.save(existPackage.get());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Update successful", existPackage));
        }
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Package does not exist", null));

    }

    @Override
    public ResponseEntity<ResponseObj> delete(Long id) {
        return null;
    }
}

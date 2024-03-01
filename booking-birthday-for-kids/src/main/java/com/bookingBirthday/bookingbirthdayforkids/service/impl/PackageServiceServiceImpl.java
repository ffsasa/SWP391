package com.bookingBirthday.bookingbirthdayforkids.service.impl;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.PackageServiceRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.model.PackageService;
import com.bookingBirthday.bookingbirthdayforkids.repository.PackageServiceRepository;
import com.bookingBirthday.bookingbirthdayforkids.service.PackageServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Service
public class PackageServiceServiceImpl implements PackageServiceService {
    @Autowired
    PackageServiceRepository packageServiceRepository;
    @Override
    public ResponseEntity<ResponseObj> getAll() {
        List<PackageService> packageServiceList = packageServiceRepository.findAll();
        if (packageServiceList.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "List is empty", null));

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.OK.toString(), "OK", packageServiceList));
    }

    @Override
    public ResponseEntity<ResponseObj> getById(Long id) {
        try {
            Optional<PackageService> packageService = packageServiceRepository.findById(id);
            if(packageService.isPresent())
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), null, packageService));
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Package Service does not exist", null));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> create(PackageServiceRequest packageServiceRequest) {
        PackageService packageService = new PackageService();
        packageService.setPackageID(packageServiceRequest.getPackageId());
        packageService.setServiceID(packageServiceRequest.getServiceId());
        packageService.setPricing(packageServiceRequest.getPricing());
        packageService.setCount(packageServiceRequest.getCount());
        packageService.setActive(true);
        packageService.setCreateAt(LocalDateTime.now());
        packageService.setUpdateAt(LocalDateTime.now());
        packageServiceRepository.save(packageService);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(),"Create successful", packageService));
    }

    @Override
    public ResponseEntity<ResponseObj> update(Long id, PackageServiceRequest packageServiceRequest) {
        Optional<PackageService> existPackageService  = packageServiceRepository.findById(id);
        if (existPackageService.isPresent()){
            existPackageService.get().setServiceID(packageServiceRequest.getServiceId() == 0 ? existPackageService.get().getPackageID() : packageServiceRequest.getPackageId());
            existPackageService.get().setPackageID(packageServiceRequest.getPackageId() == 0 ? existPackageService.get().getServiceID() : packageServiceRequest.getServiceId());
            existPackageService.get().setCount(packageServiceRequest.getCount() == 0 ? existPackageService.get().getCount() : packageServiceRequest.getCount());
            existPackageService.get().setPricing(packageServiceRequest.getPricing() == 0 ? existPackageService.get().getPricing() : packageServiceRequest.getPricing());
            existPackageService.get().setUpdateAt(LocalDateTime.now());
            packageServiceRepository.save(existPackageService.get());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Update successful", existPackageService));
        }
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Package Service does not exist", null));

    }

    @Override
    public ResponseEntity<ResponseObj> delete(Long id) {
        Optional<PackageService> packageService = packageServiceRepository.findById(id);
        if (packageService.isPresent()){
            packageService.get().setActive(false);
            packageService.get().setDeleteAt(LocalDateTime.now());
            packageServiceRepository.save(packageService.get());
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Delete successful", null));
        }
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Package Service does not exist", null));
    }
}

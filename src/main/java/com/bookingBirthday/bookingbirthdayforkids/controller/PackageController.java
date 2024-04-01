package com.bookingBirthday.bookingbirthdayforkids.controller;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.PackageServiceRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.model.TypeEnum;
import com.bookingBirthday.bookingbirthdayforkids.service.PackageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/package")
public class PackageController {
    @Autowired
    PackageService packageService;

    @GetMapping("/get-all-package-for-customer/{venueId}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<ResponseObj> getAllForCustomer(@PathVariable Long venueId,
                                                         @RequestParam(required = false, defaultValue = "") String packageType) {
        if (packageType.isEmpty()) {
            return packageService.getAllForCustomer(venueId);
        } else {
            TypeEnum parseEnum = TypeEnum.valueOf(packageType);
            return packageService.getAllForCustomerByType(venueId, parseEnum);
        }
    }


    @GetMapping("/get-all-package-for-host")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HOST')")
    public ResponseEntity<ResponseObj> getAllForHost(
            @RequestParam(required = false, defaultValue = "") String active,
            @RequestParam(required = false, defaultValue = "") String packageType
    ) {
        if (active.isEmpty() && packageType.isEmpty()) {
            return packageService.getAllForHost();
        } else if (!active.isEmpty() && packageType.isEmpty()) {
            boolean isActive = Boolean.parseBoolean(active);
            if (isActive) {
                return packageService.getAllForHostIsTrue();
            } else {
                return packageService.getAllForHostIsFalse();
            }
        } else if (active.isEmpty() && !packageType.isEmpty()) {
            TypeEnum typeEnum = TypeEnum.valueOf(packageType);
            return packageService.getAllForHostByType(typeEnum);
        } else{
            boolean isActive = Boolean.parseBoolean(active);
            TypeEnum typeEnum = TypeEnum.valueOf(packageType);
            if (isActive) {
                return packageService.getAllForHostIsTrueByType(typeEnum);
            } else {
                return packageService.getAllForHostIsFalseByType(typeEnum);
            }
        }
    }



    @GetMapping("/get-package-for-customer/{packageId}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<ResponseObj> getByIdForCustomer( @PathVariable Long packageId) {
        return packageService.getByIdForCustomer(packageId);
    }

    @GetMapping("/get-package-for-host/{packageId}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HOST')")
    public ResponseEntity<ResponseObj> getByIdForHost(@PathVariable Long packageId) {
        return packageService.getByIdForHost(packageId);
    }


    @PreAuthorize("hasAuthority('HOST')")
    @PostMapping(value = "/create-package", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> create(
                                    @RequestPart(name = "fileImg", required = true) MultipartFile fileImg,
                                    @RequestPart(name = "packageName") String packageName,
                                    @RequestPart(name = "packageDescription") String packageDescription,
                                    @RequestPart(name = "percent") String percent,
                                    @RequestPart(name = "packageServiceRequests") String packageServiceRequestsStr,
                                    @RequestPart(name = "packageType") String typeEnum) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            float parsePercent = Float.parseFloat(percent);
            TypeEnum parseTypeEnum = TypeEnum.valueOf(typeEnum);
            if (parsePercent > 0.5 || parsePercent < 0.1)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Percent ranges from 0.1-0.5", null));
            List<PackageServiceRequest> packageServiceRequests = objectMapper.readValue(packageServiceRequestsStr, new TypeReference<List<PackageServiceRequest>>() {
            });
            return packageService.create(fileImg, packageName, packageDescription, parsePercent, packageServiceRequests, parseTypeEnum);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Invalid percent", null));
        }

    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HOST')")
    @PutMapping(value = "/update-package/{packageId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> update( @PathVariable Long packageId, @RequestPart(name = "fileImg", required = false) MultipartFile fileImg, @RequestPart String packageName, @RequestPart String packageDescription) {
        return packageService.update(packageId, fileImg, packageName, packageDescription);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HOST')")
    @PatchMapping(value = "/update-percent-package/{packageId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updatePercentPackage(@PathVariable Long packageId, @RequestPart String percent) {
        try {
            float parsePercent = Float.parseFloat(percent);
            if (parsePercent > 0.5 || parsePercent < 0.1) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Percent ranges from 0.1-0.5", null));
            }
            return packageService.updatePercentPackage(packageId, Float.parseFloat(percent));
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Invalid percent", null));
        }
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HOST')")
    @DeleteMapping("/delete/{packageId}")
    public ResponseEntity<ResponseObj> delete( @PathVariable Long packageId) {
        return packageService.delete(packageId);
    }
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HOST')")
    @PutMapping("/enable/{packageId}")
    public ResponseEntity<ResponseObj> enablePackageForHost(@PathVariable Long packageId) {
        return packageService.enablePackageForHost(packageId);
    }
}

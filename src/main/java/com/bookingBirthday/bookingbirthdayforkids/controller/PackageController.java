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

import java.util.List;

@RestController
@RequestMapping("/api/package")
public class PackageController {
    @Autowired
    PackageService packageService;

    @GetMapping("/get-all-package-for-customer")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<ResponseObj> getAllForCustomer() {
        return packageService.getAllForCustomer();
    }

    @GetMapping("/get-all-package-for-host")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HOST')")
    public ResponseEntity<ResponseObj> getAllForHost() {
        return packageService.getAllForHost();
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
    @PostMapping(value = "/create-package/{venueId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> create(@PathVariable Long venueId,
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
            return packageService.create(venueId, fileImg, packageName, packageDescription, parsePercent, packageServiceRequests, parseTypeEnum);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Invalid percent", null));
        }

    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HOST')")
    @PutMapping(value = "/update-package/{venueId}/{packageId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> update(@PathVariable Long venueId, @PathVariable Long packageId, @RequestPart(name = "fileImg", required = false) MultipartFile fileImg, @RequestPart String packageName, @RequestPart String packageDescription) {
        return packageService.update(venueId, packageId, fileImg, packageName, packageDescription);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HOST')")
    @PatchMapping(value = "/update-percent-package/{venueId}/{packageId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updatePercentPackage(@PathVariable Long venueId, @PathVariable Long packageId, @RequestPart String percent) {
        try {
            float parsePercent = Float.parseFloat(percent);
            if (parsePercent > 0.5 || parsePercent < 0.1) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Percent ranges from 0.1-0.5", null));
            }
            return packageService.updatePercentPackage(venueId, packageId, Float.parseFloat(percent));
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

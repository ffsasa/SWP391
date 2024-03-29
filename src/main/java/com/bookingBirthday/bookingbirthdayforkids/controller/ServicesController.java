package com.bookingBirthday.bookingbirthdayforkids.controller;

import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.model.TypeEnum;
import com.bookingBirthday.bookingbirthdayforkids.service.ServicesService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/services")

public class ServicesController {
    @Autowired
    ServicesService servicesService;

    @GetMapping("/getAll-service-for-customer-by-venue/{venueId}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<ResponseObj> getAllForCustomerByVenue(@PathVariable Long venueId){
        return servicesService.getAllServiceByVenue(venueId);
    }

    @GetMapping("/getId-service-by-id-for-customer/{venueId}/{serviceId}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<ResponseObj> getByIdForCustomer(@PathVariable Long venueId, @PathVariable Long serviceId){
        return servicesService.getServiceByIdForCustomeByVenue(venueId, serviceId);
    }

    @GetMapping("/get-service-by-type-for-customer/{serviceType}/{venueId}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<ResponseObj> getServiceByType(@RequestParam(name = "serviceType") String serviceType, @PathVariable Long venueId){
        TypeEnum typeEnum = TypeEnum.valueOf(serviceType);
        return servicesService.getAllServiceByTypeByVenue(typeEnum, venueId);
    }

    @GetMapping("/getAll-service-for-host")
    @PreAuthorize("hasAuthority('HOST')")
    public ResponseEntity<ResponseObj> getAllForHost(){
        return servicesService.getAllForHost();
    }


    @GetMapping("/get-services-id-for-host/{id}")
    @PreAuthorize("hasAuthority('HOST')")
    public ResponseEntity<ResponseObj> getByIdForHost(@PathVariable Long id){
        return servicesService.getByIdByHost(id);
    }

    @GetMapping("/get-service-by-type-for-customer/{serviceType}")
    @PreAuthorize("hasAuthority('HOST')")
    public ResponseEntity<ResponseObj> getServiceByTypeForHost(@RequestParam String serviceType){
        TypeEnum typeEnum = TypeEnum.valueOf(serviceType);
        return servicesService.getAllServiceTypeByHost(typeEnum);
    }


    @PreAuthorize("hasAuthority('HOST')")
    @PostMapping(value = "/create-service", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> create(@RequestPart(name = "fileImg", required = true) MultipartFile fileImg,
                                    @RequestPart(name = "serviceName") String serviceName,
                                    @RequestPart(name = "serviceDescription") String description,
                                    @RequestPart(name = "pricing") String pricing,
                                    @RequestPart(name = "serviceType") String typeEnum) throws JsonProcessingException {
        try {
            float parsedPricing = Float.parseFloat(pricing);
            TypeEnum parseTypeEnum = TypeEnum.valueOf(typeEnum);
            return servicesService.create( fileImg, serviceName, description, parsedPricing, parseTypeEnum);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Invalid pricing", null));
        }
    }

    @PreAuthorize("hasAuthority('HOST')")
    @DeleteMapping("/delete-service/{id}")
    public ResponseEntity<ResponseObj> delete(@PathVariable Long id){
        return servicesService.delete(id);
    }

    @PreAuthorize("hasAuthority('HOST')")
    @PutMapping(value = "/update-service/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestPart(name = "fileImg", required = false) MultipartFile fileImg,
                                    @RequestPart(name = "serviceName") String serviceName,
                                    @RequestPart(name = "serviceDescription") String description,
                                    @RequestPart(name = "pricing") String pricing,
                                    @RequestPart(name = "serviceType") String typeEnum) throws JsonProcessingException {
        try {
            float parsedPricing = Float.parseFloat(pricing);
            TypeEnum parseTypeEnum = TypeEnum.valueOf(typeEnum);
            return servicesService.update(id, fileImg, serviceName, description, parsedPricing, parseTypeEnum);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Invalid pricing", null));
        }
    }
}

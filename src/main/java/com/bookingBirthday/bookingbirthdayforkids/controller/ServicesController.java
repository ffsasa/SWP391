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

    @GetMapping("/getAll-service")
    public ResponseEntity<ResponseObj> getAll(){
        return servicesService.getAll();
    }

    @GetMapping("/getAll-service-for-host")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HOST')")
    public ResponseEntity<ResponseObj> getAllForHost(){
        return servicesService.getAllForHost();
    }

    @GetMapping("/getId-service/{id}")
    public ResponseEntity<ResponseObj> getByid(@PathVariable Long id){
        return servicesService.getById(id);
    }

    @GetMapping("/get-services-for-customer-id/{id}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<ResponseObj> getById_ForCustomer(@PathVariable Long id){
        return servicesService.getById_ForCustomer(id);
    }

    @GetMapping("/get-service-by-type")
    public ResponseEntity<ResponseObj> getServiceByType(@PathVariable(name = "serviceType") TypeEnum typeEnum){
        return servicesService.getAllServiceByType(typeEnum);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HOST')")
    @PostMapping(value = "/create-service", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> create(@RequestPart(name = "fileImg", required = true) MultipartFile fileImg,
                                    @RequestPart(name = "serviceName") String serviceName,
                                    @RequestPart(name = "serviceDescription") String description,
                                    @RequestPart(name = "pricing") String pricing,
                                    @RequestPart(name = "serviceType") TypeEnum typeEnum) throws JsonProcessingException {
        try {
            float parsedPricing = Float.parseFloat(pricing);
            return servicesService.create( fileImg, serviceName, description, parsedPricing, typeEnum);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Invalid pricing", null));
        }
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HOST')")
    @DeleteMapping("/delete-service/{id}")
    public ResponseEntity<ResponseObj> delete(@PathVariable Long id){
        return servicesService.delete(id);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HOST')")
    @PutMapping(value = "/update-service/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestPart(name = "fileImg", required = false) MultipartFile fileImg,
                                    @RequestPart(name = "serviceName") String serviceName,
                                    @RequestPart(name = "serviceDescription") String description,
                                    @RequestPart(name = "pricing") String pricing,
                                    @RequestPart(name = "serviceType") TypeEnum typeEnum) throws JsonProcessingException {
        try {
            float parsedPricing = Float.parseFloat(pricing);
            return servicesService.update(id, fileImg, serviceName, description, parsedPricing, typeEnum);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Invalid pricing", null));
        }
    }
}

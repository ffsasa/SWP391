package com.bookingBirthday.bookingbirthdayforkids.controller;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.PartyBookingRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.request.UpgradeServiceRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObjMeta;
import com.bookingBirthday.bookingbirthdayforkids.model.StatusEnum;
import com.bookingBirthday.bookingbirthdayforkids.model.UpgradeService;
import com.bookingBirthday.bookingbirthdayforkids.service.PartyBookingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/party-booking")
public class PartyBookingController {

    @Autowired
    PartyBookingService partyBookingService;

    @GetMapping("/get-all-by-user")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<ResponseObj> getAllByUser() {
        return partyBookingService.getAllByUser();
    }

    @GetMapping("/get-all-party-booking-for-host")
    @PreAuthorize("hasAuthority('HOST')")
    public ResponseEntity<ResponseObjMeta> getAllForHost(@RequestParam(required = false, defaultValue = "") LocalDate date,
                                                         @RequestParam(required = false, defaultValue = "") String status,
                                                         @RequestParam(required = false, defaultValue = "") LocalDate createdDate,
                                                         @RequestParam(required = false, defaultValue = "1") int page,
                                                         @RequestParam(required = false, defaultValue = "4") int size) {
        if (date != null){
            if (!status.isEmpty()){
                if (createdDate != null){
                    StatusEnum statusEnum = StatusEnum.valueOf(status);
                    return partyBookingService.getAll_ForHostByDateAndCreatedAndStatus(date, createdDate, statusEnum, page, size);
                }
                return partyBookingService.getAll_ForHostByTypeAndDate(StatusEnum.valueOf(status), date, page, size);
            } else {
                if (createdDate != null){
                    return partyBookingService.getAll_ForHostByDateAndCreated(date, createdDate, page, size);
                }
                return partyBookingService.getAll_ForHostByDate(date, page, size);
            }
        } else {
            if (!status.isEmpty()){
                if (createdDate != null){
                    return partyBookingService.getAll_ForHostByStatusAndCreated(StatusEnum.valueOf(status), createdDate, page, size);
                }
                return partyBookingService.getAll_ForHostByStatus(StatusEnum.valueOf(status), page, size);
            } else {
                if (createdDate != null){
                    return partyBookingService.getAll_ForHostByCreated(createdDate, page, size);
                }
                return partyBookingService.getAll_ForHost(page, size);
            }
        }
    }

    @GetMapping("/get-all-completed")
    @PreAuthorize("hasAuthority('HOST')")
    public ResponseEntity<ResponseObjMeta> getAllCompleted(@RequestParam(required = false, defaultValue = "1") int page,
                                                       @RequestParam(required = false, defaultValue = "4") int size) {
        return partyBookingService.getAllCompleted(page, size);
    }

    @GetMapping("/get-by-id-for-host/{partyBookingId}")
    @PreAuthorize("hasAuthority('HOST')")
    public ResponseEntity<ResponseObj> getByIdForHost(@PathVariable Long partyBookingId) {
        return partyBookingService.getById_ForHost(partyBookingId);
    }

    @GetMapping("/get-by-id-for-customer/{partyBookingId}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<ResponseObj> getById_ForCustomer(@PathVariable Long partyBookingId) {
        return partyBookingService.getById_ForCustomer(partyBookingId);
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<ResponseObj> create(@Valid @RequestBody PartyBookingRequest partyBookingRequest) {
        return partyBookingService.create(partyBookingRequest);
    }

    @PutMapping("/update-upgrade-service/{partyBookingId}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<ResponseObj> updateUpgradeService(@PathVariable Long partyBookingId, @RequestParam(required = false, defaultValue = "") String dataUpgrade) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<UpgradeServiceRequest> dataUpgradeList = objectMapper.readValue(dataUpgrade, new TypeReference<List<UpgradeServiceRequest>>() {
        });
        return partyBookingService.updateUpgradeService(partyBookingId, dataUpgradeList);
    }

    @PutMapping("/update-organization-time/{partyBookingId}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<ResponseObj> updateOrganizationTime(@PathVariable Long partyBookingId,
                                                              @RequestParam(required = false, defaultValue = "") Long slotInRoomId,
                                                              @RequestParam(required = false, defaultValue = "") LocalDate date) {
        return partyBookingService.updateOrganizationTime(partyBookingId, date, slotInRoomId);
    }

    @PutMapping("/update-package/{partyBookingId}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<ResponseObj> updatePackage(@PathVariable Long partyBookingId,
                                                     @RequestParam(required = false, defaultValue = "") Long packageDecoId,
                                                     @RequestParam(required = false, defaultValue = "") Long packageFoodId) {
        return partyBookingService.updatePackage(partyBookingId, packageDecoId, packageFoodId);
    }

    @PutMapping("/update-basic-info/{partyBookingId}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<ResponseObj> updateBasicInfo(@PathVariable Long partyBookingId,
                                                       @RequestParam(required = false, defaultValue = "") String kidName,
                                                       @RequestParam(required = false, defaultValue = "") String reservationAgent,
                                                       @RequestParam(required = false, defaultValue = "") LocalDate kidDOB,
                                                       @RequestParam(required = false, defaultValue = "") String email,
                                                       @RequestParam(required = false, defaultValue = "") String phone,
                                                       @RequestParam(required = false, defaultValue = "") int participantAmount) {
        return partyBookingService.updateBasicInfo(partyBookingId, kidName, reservationAgent, kidDOB, email, phone, participantAmount);
    }

    @PutMapping("/cancel-party-booking-for-host/{partyBookingId}")
    @PreAuthorize("hasAuthority('HOST')")
    public ResponseEntity<ResponseObj> cancelBookingForHost(@PathVariable Long partyBookingId) {
        return partyBookingService.cancelBooking_ForHost(partyBookingId);
    }

    @PutMapping("/cancel-party-booking-for-customer/{partyBookingId}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<ResponseObj> cancelBookingForCustomer(@PathVariable Long partyBookingId) {
        return partyBookingService.cancelBooking_ForCustomer(partyBookingId);
    }

    @DeleteMapping("/delete-party-booking-for-host/{partyBookingId}")
    @PreAuthorize("hasAuthority('HOST')")
    public ResponseEntity<ResponseObj> deleteBooking(@PathVariable Long partyBookingId) {
        return partyBookingService.deleteBooking_ForHost(partyBookingId);
    }

    @PutMapping("/complete-party-booking-for-host/{partyBookingId}")
    @PreAuthorize("hasAuthority('HOST')")
    public ResponseEntity<ResponseObj> completeBookingForHost(@PathVariable Long partyBookingId) {
        return partyBookingService.completeBooking_ForHost(partyBookingId);
    }
}

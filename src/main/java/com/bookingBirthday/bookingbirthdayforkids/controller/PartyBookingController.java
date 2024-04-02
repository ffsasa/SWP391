package com.bookingBirthday.bookingbirthdayforkids.controller;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.PartyBookingRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.model.StatusEnum;
import com.bookingBirthday.bookingbirthdayforkids.service.PartyBookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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
    public ResponseEntity<ResponseObj> getAllForHost(@RequestParam(required = false, defaultValue = "") LocalDate date,
                                                     @RequestParam(required = false, defaultValue = "") String status,
                                                     @RequestParam(required = false, defaultValue = "") String active) {
        if (date == null) {
            if (status.isEmpty()) {
                return partyBookingService.getAll_ForHost();
            }
            return partyBookingService.getAll_ForHostByType(StatusEnum.valueOf(status));
        } else {
            if (!status.isEmpty()) {
                StatusEnum statusEnum = StatusEnum.valueOf(status);
                return partyBookingService.getAll_ForHostByTypeAndDate(statusEnum, date);
            }
            return partyBookingService.getAll_ForHostByDate(date);
        }
    }

    @GetMapping("/get-all-completed")
    @PreAuthorize("hasAuthority('HOST')")
    public ResponseEntity<ResponseObj> getAllCompleted() {
        return partyBookingService.getAllCompleted();
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
    public ResponseEntity<ResponseObj> updateUpgradeService(@PathVariable Long partyBookingId, @Valid @RequestBody PartyBookingRequest partyBookingRequest) {
        return partyBookingService.updateUpgradeService(partyBookingId, partyBookingRequest);
    }

    @PutMapping("/update-organization-time/{partyBookingId}/{slotInRoomId}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<ResponseObj> updateOrganizationTime(@PathVariable Long partyBookingId, @PathVariable Long slotInRoomId, @Valid @RequestBody LocalDate date) {
        return partyBookingService.updateOrganizationTime(partyBookingId, date, slotInRoomId);
    }

    @PutMapping("/update-package/{partyBookingId}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<ResponseObj> updatePackage(@PathVariable Long partyBookingId, @Valid @RequestBody PartyBookingRequest partyBookingRequest) {
        return partyBookingService.updatePackage(partyBookingId, partyBookingRequest);
    }

    @PutMapping("/update-basic-info/{partyBookingId}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<ResponseObj> updateBasicInfo(@PathVariable Long partyBookingId, @Valid @RequestBody PartyBookingRequest partyBookingRequest) {
        return partyBookingService.updateBasicInfo(partyBookingId, partyBookingRequest);
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

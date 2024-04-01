package com.bookingBirthday.bookingbirthdayforkids.service;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.PartyBookingRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.model.PartyBooking;
import com.bookingBirthday.bookingbirthdayforkids.model.SlotInRoom;
import com.bookingBirthday.bookingbirthdayforkids.model.StatusEnum;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface PartyBookingService {
    public ResponseEntity<ResponseObj> getAllByUser();

    public ResponseEntity<ResponseObj> getAll_ForHost();

    public ResponseEntity<ResponseObj> getAllCompleted();

    public ResponseEntity<ResponseObj> getById_ForHost(Long partyBookingId);

    public ResponseEntity<ResponseObj> getById_ForCustomer(Long partyBookingId);

    ResponseEntity<ResponseObj> getAll_ForHostByDate(LocalDate date);

    ResponseEntity<ResponseObj> getAll_ForHostByType(StatusEnum statusEnum);

    public ResponseEntity<ResponseObj> create(PartyBookingRequest partyBookingRequest);

    public ResponseEntity<ResponseObj> updateUpgradeService(Long partyBookingId, PartyBookingRequest partyBookingRequest);

    public ResponseEntity<ResponseObj> updateOrganizationTime(Long partyBookingId, LocalDate date, Long slotInRoomId);

    public ResponseEntity<ResponseObj> updatePackage(Long partyBookingId, PartyBookingRequest partyBookingRequest);

    public ResponseEntity<ResponseObj> updateBasicInfo(Long partyBookingId, PartyBookingRequest partyBookingRequest);

    ResponseEntity<ResponseObj> cancelBooking_ForHost(Long partyBookingId);

    ResponseEntity<ResponseObj> cancelBooking_ForCustomer(Long partyBookingId);

    public ResponseEntity<ResponseObj> deleteBooking_ForHost(Long partyBookingId);

    ResponseEntity<ResponseObj> completeBooking_ForHost(Long partyBookingId);

    public List<PartyBooking> findConfirmedBookings();

    public void updateCronJob(Long partyBookingId, PartyBooking partyBooking);
}

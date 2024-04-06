package com.bookingBirthday.bookingbirthdayforkids.config;

import com.bookingBirthday.bookingbirthdayforkids.model.PartyBooking;
import com.bookingBirthday.bookingbirthdayforkids.model.StatusEnum;
import com.bookingBirthday.bookingbirthdayforkids.service.PartyBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@Component
public class PartyBookingCronJob {
    @Autowired
    PartyBookingService partyBookingService;


    @Scheduled(fixedRate = 900000)
    public void processConfirmPartyBookings() {
        LocalDateTime currentTime = LocalDateTime.now();
        List<PartyBooking> confirmedBookings = partyBookingService.findConfirmedBookings();
        for (PartyBooking booking : confirmedBookings) {
            try {
                Time time = Time.valueOf(booking.getSlotInRoom().getSlot().getTimeEnd());

                LocalTime localTime = time.toLocalTime();

                LocalDateTime localDateTime = LocalDateTime.of(booking.getDate(), localTime);

                if (currentTime.isAfter(localDateTime.plusMinutes(15))) {
                    booking.setStatus(StatusEnum.COMPLETED);
                    partyBookingService.updateCronJob(booking.getId(), booking);
                }
            } catch (DateTimeParseException e) {
                System.err.println("Error parsing time: " + e.getMessage());
            }
        }
    }

    @Scheduled(fixedRate = 900000)
    public void processCancelPartyBookings() {
        LocalDateTime currentTime = LocalDateTime.now();
        List<PartyBooking> pendingBookings = partyBookingService.findPendingBookings();
        for (PartyBooking booking : pendingBookings) {
            try {
                if (currentTime.isAfter(booking.getCreateAt().plusHours(0).plusMinutes(30))){
                    booking.setStatus(StatusEnum.CANCELLED);
                    partyBookingService.updateCronJob(booking.getId(), booking);
                }
            } catch (DateTimeParseException e) {
                System.err.println("Error parsing time: " + e.getMessage());
            }
        }
    }
}

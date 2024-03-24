package com.bookingBirthday.bookingbirthdayforkids.config;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.PartyBookingRequest;
import com.bookingBirthday.bookingbirthdayforkids.model.PartyBooking;
import com.bookingBirthday.bookingbirthdayforkids.model.StatusEnum;
import com.bookingBirthday.bookingbirthdayforkids.service.PartyBookingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.util.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Component
public class PartyBookingCronJob {
    @Autowired
    PartyBookingService partyBookingService;


    @Scheduled(fixedRate = 90000)
    public void processConfirmPartyBookings() {
        LocalDateTime currentTime = LocalDateTime.now();
        List<PartyBooking> confirmedBookings = partyBookingService.findConfirmedBookings();
        for (PartyBooking booking : confirmedBookings) {
            try {
                Time time = Time.valueOf(booking.getPartyDated().getSlotInVenue().getSlot().getTimeEnd());

                LocalTime localTime = time.toLocalTime();

                LocalDateTime localDateTime = LocalDateTime.of(booking.getPartyDated().getDate(), localTime);

                if (currentTime.isAfter(localDateTime.plusMinutes(15))) {
                    booking.setStatus(StatusEnum.COMPLETED);
                    partyBookingService.updateCronJob(booking.getId(), booking);
                }
            } catch (DateTimeParseException e) {
                System.err.println("Error parsing time: " + e.getMessage());
            }
        }
    }
}

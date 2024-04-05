package com.bookingBirthday.bookingbirthdayforkids.service.impl;

import com.bookingBirthday.bookingbirthdayforkids.dto.response.DashboardResponse;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.model.*;
import com.bookingBirthday.bookingbirthdayforkids.model.Package;
import com.bookingBirthday.bookingbirthdayforkids.repository.*;
import com.bookingBirthday.bookingbirthdayforkids.service.DashboardService;
import com.bookingBirthday.bookingbirthdayforkids.util.AuthenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private PartyBookingRepository partyBookingRepository;

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private ServicesRepository servicesRepository;

    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public ResponseEntity<ResponseObj> getDashboard() {
        try {
            Long userId = AuthenUtil.getCurrentUserId();
            Account account = accountRepository.findById(userId).get();
            Venue venue = account.getVenue();
            if (venue == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This venue does not exist", null));
            }
            List<Services> servicesList = servicesRepository.findAllByAccountId(userId);
            List<DashboardResponse> servicesListResponse = new ArrayList<>();
            List<Package> packageList = packageRepository.findAllByVenueId(venue.getId());
            List<DashboardResponse> packageListResponse = new ArrayList<>();
            List<Review> reviewList = reviewRepository.findAllByVenueId(venue.getId());

            List<Slot> slotList = account.getSlotList();
            List<PartyBooking> partyBookingList = new ArrayList<>();

            if (!slotList.isEmpty()) {
                for (Slot slot : slotList) {
                    for (SlotInRoom slotInRoom : slot.getSlotInRoom()) {
                        partyBookingList.addAll(slotInRoom.getPartyBookingList());
                    }
                }
            }

            float totalRevenue = 0;
            float countCancel = 0;
            float countTransferred = 0;
            for (PartyBooking partyBooking : partyBookingList) {
                if (partyBooking.getStatus() == StatusEnum.COMPLETED || partyBooking.getStatus() == StatusEnum.CONFIRMED) {
                    totalRevenue = totalRevenue + partyBooking.getTotalPrice();
                    countTransferred++;
                }
                if (partyBooking.getStatus() == StatusEnum.CANCELLED) {
                    countCancel++;
                }
            }

            for (Package aPackage : packageList) {
                int count = 0;
                for (PartyBooking partyBooking : partyBookingList) {
                    if (partyBooking.getStatus() == StatusEnum.COMPLETED) {
                        for (PackageInBooking packageInBooking : partyBooking.getPackageInBookings()) {
                            if (packageInBooking.getAPackage().getId().equals(aPackage.getId())) {
                                count++;
                            }
                        }
                    }
                }
                DashboardResponse dashboardResponse = new DashboardResponse(aPackage.getPackageName(), count);
                packageListResponse.add(dashboardResponse);
            }

            for (Services services : servicesList) {
                int count = 0;
                for (PartyBooking partyBooking : partyBookingList) {
                    for (UpgradeService upgradeService : partyBooking.getUpgradeServices()) {
                        if (upgradeService.getServices().getId().equals(services.getId()) && partyBooking.getStatus() == StatusEnum.COMPLETED) {
                            count++;
                        }
                    }
                }
                DashboardResponse dashboardResponse = new DashboardResponse(services.getServiceName(), count);
                servicesListResponse.add(dashboardResponse);
            }

            float countReview = 0;
            for (Review review : reviewList) {
                countReview += review.getRating();
            }

            Dashboard dashboard = new Dashboard();
            dashboard.setTotalRevenue(totalRevenue);
            dashboard.setTotalBooking(partyBookingList.size());
            dashboard.setAPackageList(packageListResponse);
            dashboard.setServiceList(servicesListResponse);
            dashboard.setAverageValueOfOrders(totalRevenue / countTransferred);
            dashboard.setAverageRate(countReview / reviewList.size());
            dashboard.setPartyCancellationRate(countCancel / partyBookingList.size());

            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", dashboard));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }
}

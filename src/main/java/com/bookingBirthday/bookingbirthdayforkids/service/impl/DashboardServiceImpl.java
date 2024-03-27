package com.bookingBirthday.bookingbirthdayforkids.service.impl;

import com.bookingBirthday.bookingbirthdayforkids.dto.response.DashboardResponse;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.model.*;
import com.bookingBirthday.bookingbirthdayforkids.model.Package;
import com.bookingBirthday.bookingbirthdayforkids.repository.*;
import com.bookingBirthday.bookingbirthdayforkids.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

//    @Override
//    public ResponseEntity<ResponseObj> getDashboard() {
//        try {
//            List<Theme> themeList = themeRepository.findAll();
//            List<DashboardResponse> themeListResponse = new ArrayList<>();
//            List<Services> servicesList = servicesRepository.findAll();
//            List<DashboardResponse> servicesListResponse = new ArrayList<>();
//            List<Package> packageList = packageRepository.findAll();
//            List<DashboardResponse> packageListResponse = new ArrayList<>();
//            List<Venue> venueList = venueRepository.findAll();
//            List<DashboardResponse> venueListResponse = new ArrayList<>();
//            List<PartyBooking> partyBookingList = partyBookingRepository.findAll();
//            List<Review> reviewList = reviewRepository.findAll();
//            List<Account> accountList = accountRepository.findAll();
//
//            float totalRevenue = 0;
//            float CountCancel = 0;
//            for (PartyBooking partyBooking : partyBookingList) {
//                if (partyBooking.getStatus() == StatusEnum.COMPLETED) {
//                    float price = 0;
//                    for (UpgradeService upgradeService : partyBooking.getUpgradeServices()) {
//                        price += upgradeService.getServices().getPricing() * upgradeService.getCount();
//                    }
//                    price = price + partyBooking.getPackageInVenue().getApackage().getPricing();
//                    totalRevenue = totalRevenue + price;
//                }
//                if (partyBooking.getStatus() == StatusEnum.CANCELLED) {
//                    CountCancel++;
//                }
//            }
//
////            for (Theme theme : themeList) {
////                int Count = 0;
////                for (PartyBooking partyBooking : partyBookingList) {
////                    if (partyBooking.getThemeInVenue().getTheme().getId().equals(theme.getId()) && partyBooking.getStatus() == StatusEnum.COMPLETED) {
////                        Count++;
////                    }
////                }
//                DashboardResponse dashboardResponse = new DashboardResponse(theme.getThemeName(), Count);
//                themeListResponse.add(dashboardResponse);
//            }
//
//            for (Package Apackage : packageList) {
//                int Count = 0;
//                for (PartyBooking partyBooking : partyBookingList) {
//                    if (partyBooking.getPackageInVenue().getApackage().getId().equals(Apackage.getId()) && partyBooking.getStatus() == StatusEnum.COMPLETED) {
//                        Count++;
//                    }
//                }
//                DashboardResponse dashboardResponse = new DashboardResponse(Apackage.getPackageName(), Count);
//                packageListResponse.add(dashboardResponse);
//            }
//
//            for (Venue venue : venueList) {
//                int Count = 0;
//                for (PartyBooking partyBooking : partyBookingList) {
//                    if (partyBooking.getThemeInVenue().getVenue().getId().equals(venue.getId()) && partyBooking.getStatus() == StatusEnum.COMPLETED) {
//                        Count++;
//                    }
//                }
//                DashboardResponse dashboardResponse = new DashboardResponse(venue.getVenueName(), Count);
//                venueListResponse.add(dashboardResponse);
//            }
//
//            for (Services services : servicesList) {
//                int Count = 0;
//                for (PartyBooking partyBooking : partyBookingList) {
//                    for (UpgradeService upgradeService : partyBooking.getUpgradeServices()) {
//                        if (upgradeService.getServices().getId().equals(services.getId()) && partyBooking.getStatus() == StatusEnum.COMPLETED) {
//                            Count++;
//                        }
//                    }
//                }
//                DashboardResponse dashboardResponse = new DashboardResponse(services.getServiceName(), Count);
//                servicesListResponse.add(dashboardResponse);
//            }
//
//            float countReview = 0;
//            for (Review review : reviewList) {
//                countReview += review.getRating();
//            }
//
//            List<Account> customerList = new ArrayList<>();
//            for (Account account: accountList){
//                if(account.getRole().getName().equals(RoleEnum.CUSTOMER)){
//                    customerList.add(account);
//                }
//            }
//
//            Dashboard dashboard = new Dashboard();
//            dashboard.setTotalRevenue(totalRevenue);
//            dashboard.setTotalBooking(partyBookingList.size());
//            dashboard.setThemeList(themeListResponse);
//            dashboard.setAPackageList(packageListResponse);
//            dashboard.setServiceList(servicesListResponse);
//            dashboard.setVenueList(venueListResponse);
//            dashboard.setAverageValueOfOrders(totalRevenue/partyBookingList.size());
//            dashboard.setAverageRate(countReview/reviewList.size());
//            dashboard.setPartyCancellationRate(CountCancel/partyBookingList.size());
//            dashboard.setCustomerList(customerList);
//
//            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", dashboard));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
//        }
//}
}

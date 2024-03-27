package com.bookingBirthday.bookingbirthdayforkids.model;

import com.bookingBirthday.bookingbirthdayforkids.dto.response.DashboardResponse;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Dashboard {

    private float totalRevenue;
    private int totalBooking;
    private List<DashboardResponse> serviceList;
    private List<DashboardResponse> venueList;
    private List<DashboardResponse> aPackageList;
    private float averageValueOfOrders;
    private float averageRate;
    private float partyCancellationRate;
    private List<Account> customerList;
}

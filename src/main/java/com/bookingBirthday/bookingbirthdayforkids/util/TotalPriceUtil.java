package com.bookingBirthday.bookingbirthdayforkids.util;

import com.bookingBirthday.bookingbirthdayforkids.model.PackageInBooking;
import com.bookingBirthday.bookingbirthdayforkids.model.PartyBooking;
import com.bookingBirthday.bookingbirthdayforkids.model.TypeEnum;

public class TotalPriceUtil {
    public static float getTotalPricingPackage(PartyBooking partyBooking) {
        float totalPricingPackage = 0;
        for (PackageInBooking packageInBooking : partyBooking.getPackageInBookings()){
            if(packageInBooking.getPackageInVenue().getApackage().getPackageType().equals(TypeEnum.DECORATION)){
                totalPricingPackage += packageInBooking.getPackageInVenue().getApackage().getPricing();
            }
            else {
                totalPricingPackage += (packageInBooking.getPackageInVenue().getApackage().getPricing()* partyBooking.getParticipantAmount());
            }
        }
        return totalPricingPackage;
    }
}

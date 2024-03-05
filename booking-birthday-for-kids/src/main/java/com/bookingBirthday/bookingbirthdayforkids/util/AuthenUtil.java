package com.bookingBirthday.bookingbirthdayforkids.util;

import com.bookingBirthday.bookingbirthdayforkids.model.Account;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthenUtil {
    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            Account userDetails = (Account) authentication.getPrincipal();
            return userDetails.getId(); // Giả định rằng bạn đã tùy chỉnh UserDetails để chứa ID của người dùng
        }
        return null;
    }
}

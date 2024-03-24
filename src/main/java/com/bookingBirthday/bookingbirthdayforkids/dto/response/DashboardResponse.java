package com.bookingBirthday.bookingbirthdayforkids.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {
    private String name;
    private int quantity;
}

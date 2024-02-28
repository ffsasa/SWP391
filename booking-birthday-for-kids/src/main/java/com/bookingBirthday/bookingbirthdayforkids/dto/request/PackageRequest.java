package com.bookingBirthday.bookingbirthdayforkids.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PackageRequest {
    private String packageName;
    private String packageImgUrl;
    private Float pricing;
}

package com.bookingBirthday.bookingbirthdayforkids.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponse {
    Long id;
    String username;
    String fullName;
}

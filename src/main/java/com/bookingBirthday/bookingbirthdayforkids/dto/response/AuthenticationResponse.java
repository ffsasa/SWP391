package com.bookingBirthday.bookingbirthdayforkids.dto.response;

import com.bookingBirthday.bookingbirthdayforkids.model.Role;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
    private String token;
    private Role role;
}

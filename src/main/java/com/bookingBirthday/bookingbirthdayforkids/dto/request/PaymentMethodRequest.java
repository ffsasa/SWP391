package com.bookingBirthday.bookingbirthdayforkids.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentMethodRequest {
    @NotBlank(message = "Method cannot be blank")
    private String methodName;
    @NotBlank(message = "Description cannot be blank")
    private String methodDescription;

}

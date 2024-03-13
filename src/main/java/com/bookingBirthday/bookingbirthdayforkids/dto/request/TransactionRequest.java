package com.bookingBirthday.bookingbirthdayforkids.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionRequest {

    @NotNull(message = "Payment ID cannot be null")
    private Long paymentID;
}

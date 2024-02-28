package com.bookingBirthday.bookingbirthdayforkids.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaymentMethod extends BaseEntity {
    @NotBlank(message = "Method cannot be blank")
    private String methodName;
    @NotBlank(message = "Description cannot be blank")
    private String methodDescription;

    @OneToMany(mappedBy = "paymentMethod")
    private List<Payment> paymentList;
}

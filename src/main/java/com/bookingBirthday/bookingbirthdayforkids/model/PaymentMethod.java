package com.bookingBirthday.bookingbirthdayforkids.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class PaymentMethod extends BaseEntity {
    @NotBlank(message = "Method Name cannot blank")
    private String methodName;
    @NotBlank(message = "Method Description cannot blank")
    private String methodDescription;

    @OneToMany(mappedBy = "paymentMethod", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Payment> paymentList;
}

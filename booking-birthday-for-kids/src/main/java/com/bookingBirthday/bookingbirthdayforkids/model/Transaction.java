package com.bookingBirthday.bookingbirthdayforkids.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Transaction extends BaseEntity {
    @NotBlank(message = "Status cannot be blank")
    private String status;

    @DateTimeFormat(pattern = "yyyy-mm-dd")
    @JsonIgnore
    private LocalDateTime transactionDate;

    @ManyToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;
}

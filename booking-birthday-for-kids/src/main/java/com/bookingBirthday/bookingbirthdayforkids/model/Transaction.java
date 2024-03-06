package com.bookingBirthday.bookingbirthdayforkids.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "Status cannot null")
    @JsonIgnore
    private int status;

    @DateTimeFormat(pattern = "yyyy-mm-dd")
    @JsonIgnore
    private LocalDateTime transactionDate;

    @ManyToOne
    @JoinColumn(name = "payment_id")
    @JsonBackReference
    private Payment payment;
}

package com.bookingBirthday.bookingbirthdayforkids.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Payment extends BaseEntity {
    @NotBlank(message = "amount cannot blank")
    private float amount;
    @NotBlank(message = "Exipre date cannot blank")
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDateTime expireDate;
    @JsonIgnore
    private int status;

    @ManyToOne
    @JoinColumn(name = "account_id")
    @JsonBackReference
    private Account account;

    @ManyToOne
    @JoinColumn(name = "paymentMethod_id")
    @JsonBackReference
    private PaymentMethod paymentMethod;

    @OneToMany(mappedBy = "payment")
    private List<Transaction> transactionList;


}

package com.bookingBirthday.bookingbirthdayforkids.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Verify{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne
    @JoinColumn(name = "user_account_id",referencedColumnName = "id")
    private Account account;
    @Column(nullable = false,unique = true)
    private String code;
    @Column(nullable = false)
    private LocalDateTime expiryDate;
}

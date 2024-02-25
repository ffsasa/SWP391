package com.bookingBirthday.bookingbirthdayforkids.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean isActive;
    @JsonIgnore
    @CreatedDate
    private LocalDateTime createAt;
    @JsonIgnore
    @LastModifiedDate
    private LocalDateTime updateAt;
    @JsonIgnore
    private LocalDateTime deleteAt;
}

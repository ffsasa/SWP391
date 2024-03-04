package com.bookingBirthday.bookingbirthdayforkids.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class PackageService extends BaseEntity{
    @NotNull(message = "Package ID cannot blank")
    private long packageID;
    @NotNull(message = "Service ID cannot blank")
    private long serviceID;
    @NotNull(message = "Count value cannot be null")
    private int count;
    @NotNull(message = "Pricing value cannot be null")
    @Min(value = 0, message = "Pricing value must be greater than or equal to 0")
    private float pricing;

    @ManyToOne
    @JoinColumn(name = "package_id")
    @JsonBackReference
    private Package apackage;

    @ManyToOne
    @JoinColumn(name = "service_id")
    @JsonBackReference
    private Service service;
}

package com.bookingBirthday.bookingbirthdayforkids.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PackageService extends BaseEntity{
    @NotNull(message = "Package ID cannot blank")
    private String packageID;
    @NotNull(message = "Service ID cannot blank")
    private String serviceID;
    @NotNull(message = "Count value cannot be null")
    private String Count;
    @NotNull(message = "Pricing value cannot be null")
    @Min(value = 0, message = "Pricing value must be greater than or equal to 0")
    private float pricing;
    @ManyToOne
    @JoinColumn(name = "Package")
    private Package Package;
}

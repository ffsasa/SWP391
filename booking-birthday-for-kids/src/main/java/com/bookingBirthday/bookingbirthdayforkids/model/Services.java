package com.bookingBirthday.bookingbirthdayforkids.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Services extends BaseEntity{
    @NotBlank(message = "Services name cannot be blank")
    private String serviceName;

    @NotBlank(message = "Description of services name cannot be blank")
    private String description;
    @NotNull(message = "Pricing value cannot be null")
    @Min(value = 0, message = "Pricing value must be greater than or equal to 0")
    private float pricing;

    @OneToMany(mappedBy = "services", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<PackageService> packageServiceList;

    @OneToMany(mappedBy = "services", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<UpgradeService> upgradeServiceList;

}

package com.bookingBirthday.bookingbirthdayforkids.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Slot extends BaseEntity {
    @NotNull(message = "TimeStart value cannot be null")
    @NotBlank(message = "TimeStart value cannot be blank")
    @DateTimeFormat(pattern = "HH:mm:ss")
    private String timeStart;
    @NotNull(message = "TimeEnd value cannot be null")
    @NotBlank(message = "TimeEnd value cannot be blank")
    @DateTimeFormat(pattern = "HH:mm:ss")
    private String timeEnd;
    @OneToMany(mappedBy = "slot", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<SlotInVenue> slotInVenue;
    }

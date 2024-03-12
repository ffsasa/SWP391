package com.bookingBirthday.bookingbirthdayforkids.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Slot extends BaseEntity{
    @NotNull(message = "TimeStart value cannot be null")
    @NotBlank(message = "TimeStart value cannot be blank")
    @DateTimeFormat(pattern = "HH:mm:ss")
    private String timeStart;
    @NotNull(message = "TimeEnd value cannot be null")
    @NotBlank(message = "TimeEnd value cannot be blank")
    @DateTimeFormat(pattern = "HH:mm:ss")
    private String timeEnd;
    @OneToMany(mappedBy = "slot", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    @JsonIgnore
    private List<SlotInVenue> slotInVenueList;
    public boolean isValidTimeRange() {
        try {
            LocalTime start = LocalTime.parse(this.timeStart, DateTimeFormatter.ofPattern("HH:mm:ss"));
            LocalTime end = LocalTime.parse(this.timeEnd, DateTimeFormatter.ofPattern("HH:mm:ss"));
            return start.isBefore(end);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid time format");
        }
    }
    //
}

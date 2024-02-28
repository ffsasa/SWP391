package com.bookingBirthday.bookingbirthdayforkids.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class PartyDated extends BaseEntity{
    private long slotID;
    private long venueID;
    @JsonIgnore
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDateTime Date;
    @ManyToOne
    @JoinColumn(name = "Slot")
    private Slot slot;

}

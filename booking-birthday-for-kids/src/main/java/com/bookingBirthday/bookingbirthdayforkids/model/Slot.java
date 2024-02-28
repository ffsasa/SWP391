package com.bookingBirthday.bookingbirthdayforkids.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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
public class Slot extends BaseEntity{
    @JsonIgnore
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDateTime TimeStart;

    @JsonIgnore
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDateTime TimeEnd;

    @OneToMany (mappedBy = "Slot")
    private List<PartyDated> partyDatedList;
}

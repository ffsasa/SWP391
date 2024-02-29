package com.bookingBirthday.bookingbirthdayforkids.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Slot extends BaseEntity{
    @JsonIgnore
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDateTime TimeStart;

    @JsonIgnore
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDateTime TimeEnd;

//    @OneToMany(mappedBy = "slot", cascade = CascadeType.ALL)
//    private List<PartyDated> partyDateds = new ArrayList<>();
}

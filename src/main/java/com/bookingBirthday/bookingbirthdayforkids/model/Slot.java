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
//    @JsonIgnore
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private String timeStart;

//    @JsonIgnore
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private String timeEnd;

    @OneToMany(mappedBy = "slot", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<PartyDated> partyDated;
}

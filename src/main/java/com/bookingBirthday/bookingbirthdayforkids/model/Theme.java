package com.bookingBirthday.bookingbirthdayforkids.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Theme extends BaseEntity{
    @NotBlank(message = "Theme name cannot be blank")
    private String themeName;
    @Column(name = "theme_description",columnDefinition = "TEXT")
    @NotBlank(message = "Description cannot be blank")
    private String themeDescription;
    @Column(name = "theme_img_url",columnDefinition = "TEXT")
    private String themeImgUrl;

    @OneToMany(mappedBy = "theme", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    @JsonIgnore
    private List<ThemeInVenue> themeInVenueList;
}

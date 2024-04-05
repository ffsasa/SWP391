package com.bookingBirthday.bookingbirthdayforkids.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Meta {
    private int totalItems;
    private int totalPages;
    private int currentPage;
    private int pageSize;
}

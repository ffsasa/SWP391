package com.bookingBirthday.bookingbirthdayforkids.service;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.ThemeRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import org.springframework.http.ResponseEntity;

public interface ThemeService {
    public ResponseEntity<ResponseObj> getAll();

    public ResponseEntity<ResponseObj> getById(Long id);

    public ResponseEntity<ResponseObj> create(ThemeRequest themeRequest);

    public ResponseEntity<ResponseObj> update(Long id, ThemeRequest themeRequest);

    public ResponseEntity<ResponseObj> delete(Long id);
}

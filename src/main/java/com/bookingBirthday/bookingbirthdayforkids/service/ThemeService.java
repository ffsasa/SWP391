package com.bookingBirthday.bookingbirthdayforkids.service;

import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ThemeService {
    public ResponseEntity<ResponseObj> getAll();

    public ResponseEntity<ResponseObj> getById(Long id);

    public ResponseEntity<ResponseObj> create(MultipartFile imgFile, String themeName, String themDescription);

    public ResponseEntity<ResponseObj> update(Long id, MultipartFile imgFile, String themeName, String themDescription);

    public ResponseEntity<ResponseObj> delete(Long id);
    public ResponseEntity<ResponseObj> addThemeInVenueByVenueId(Long venueId, List<Long> themeIdList);

}

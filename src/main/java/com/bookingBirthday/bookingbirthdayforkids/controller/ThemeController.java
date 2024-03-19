package com.bookingBirthday.bookingbirthdayforkids.controller;

import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.service.ThemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/theme")
public class ThemeController {

    @Autowired
    ThemeService themeService;

    @GetMapping("/get-all")
    public ResponseEntity<ResponseObj> getAll() {
        return themeService.getAll();
    }

    @GetMapping("/get-id/{id}")
    public ResponseEntity<ResponseObj> getById(@PathVariable Long id){
        return themeService.getById(id);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HOST')")
    @PostMapping(value = "/create-theme", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> create(@RequestPart(name = "fileImg", required = true) MultipartFile fileImg,
                                    @RequestPart String themeName,
                                    @RequestPart String themDescription){
        return themeService.create(fileImg, themeName, themDescription);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HOST')")
    @PutMapping(value = "/update-theme/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestPart(name = "fileImg", required = false) MultipartFile fileImg,
                                    @RequestPart String themeName,
                                    @RequestPart String themDescription){
        return themeService.update(id,fileImg, themeName, themDescription);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HOST')")
    @PostMapping(value = "/add-theme-in-venue-by-them-id/{id}")
    public ResponseEntity<?> addThemeInVenueByThemeId(@RequestParam Long themeId, @RequestBody List<Long> venueIdList){
        return themeService.addThemeInVenueByThemeId(themeId, venueIdList);
    }
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HOST')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseObj> delete(@PathVariable Long id){
        return themeService.delete(id);
    }
}

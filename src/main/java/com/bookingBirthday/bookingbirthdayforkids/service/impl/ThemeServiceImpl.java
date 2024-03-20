package com.bookingBirthday.bookingbirthdayforkids.service.impl;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.PackageServiceRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.model.Package;
import com.bookingBirthday.bookingbirthdayforkids.model.PackageInVenue;
import com.bookingBirthday.bookingbirthdayforkids.model.Theme;
import com.bookingBirthday.bookingbirthdayforkids.model.ThemeInVenue;
import com.bookingBirthday.bookingbirthdayforkids.model.Venue;
import com.bookingBirthday.bookingbirthdayforkids.repository.ThemeInVenueRepository;
import com.bookingBirthday.bookingbirthdayforkids.repository.ThemeRepository;
import com.bookingBirthday.bookingbirthdayforkids.repository.VenueRepository;
import com.bookingBirthday.bookingbirthdayforkids.service.ThemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ThemeServiceImpl implements ThemeService {

    @Autowired
    ThemeRepository themeRepository;
    @Autowired
    FirebaseService firebaseService;
    @Autowired
    VenueRepository venueRepository;
    @Autowired
    ThemeInVenueRepository themeInVenueRepository;

    @Override
    public ResponseEntity<ResponseObj> getAll() {
        try {
            List<Theme> themeList = themeRepository.findAllByIsActiveIsTrue();
            if (themeList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "List is empty", null));
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", themeList));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> getById(Long id) {
        try {
            Optional<Theme> theme = themeRepository.findById(id);
            if (theme.isPresent()) {
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", theme));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This theme does not exist", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> create(MultipartFile imgFile, String themeName, String themDescription) {
        if (themeRepository.existsByThemeName(themeName)) {
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(new ResponseObj(HttpStatus.ALREADY_REPORTED.toString(), "Theme name has already exist", null));
        }
        Theme theme = new Theme();
        try {
            if (imgFile != null) {
                String img = firebaseService.uploadImage(imgFile);
                theme.setThemeName(themeName);
                theme.setThemeDescription(themDescription);
                theme.setThemeImgUrl(img);
                theme.setActive(true);
                theme.setCreateAt(LocalDateTime.now());
                theme.setUpdateAt(LocalDateTime.now());
                themeRepository.save(theme);
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Image is invalid", null));

        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Create successful", theme));
    }

    @Override
    public ResponseEntity<ResponseObj> addThemeInVenueByVenueId(Long venueId, List<Long> themeIdList){
        Venue venue = venueRepository.findById(venueId).get();
        ThemeInVenue themeInVenue = new ThemeInVenue();

        for (Long addtheme : themeIdList){
            themeInVenue = new ThemeInVenue();
            Theme theme = themeRepository.findById(addtheme.longValue()).get();
            themeInVenue.setVenue(venue);
            themeInVenue.setTheme(theme);
            themeInVenue.setActive(true);
            themeInVenue.setCreateAt(LocalDateTime.now());
            themeInVenue.setUpdateAt(LocalDateTime.now());
            themeInVenueRepository.save(themeInVenue);
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Create successful", themeInVenue));


    }

    @Override
    public ResponseEntity<ResponseObj> update(Long id, MultipartFile imgFile, String themeName, String themDescription) {
        Optional<Theme> theme = themeRepository.findById(id);
        if (!theme.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This theme does not exist", null));
        }
        try {

            theme.get().setThemeName(themeName == null ? theme.get().getThemeName() : themeName);
            theme.get().setThemeDescription(themDescription == null ? theme.get().getThemeDescription() : themDescription);
            theme.get().setThemeImgUrl(imgFile == null ? theme.get().getThemeImgUrl() : firebaseService.uploadImage(imgFile));
            theme.get().setUpdateAt(LocalDateTime.now());
            themeRepository.save(theme.get());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Update successful", theme));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> delete(Long id) {
        try {
            Optional<Theme> theme = themeRepository.findById(id);
            if (theme.isPresent()) {
                theme.get().setActive(false);
                theme.get().setDeleteAt(LocalDateTime.now());
                themeRepository.save(theme.get());
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Delete successful", null));
            } else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This theme does not exist", null));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }
}

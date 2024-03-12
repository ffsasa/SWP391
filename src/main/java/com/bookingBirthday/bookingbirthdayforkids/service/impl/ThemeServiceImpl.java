package com.bookingBirthday.bookingbirthdayforkids.service.impl;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.ThemeRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.model.Services;
import com.bookingBirthday.bookingbirthdayforkids.model.Theme;
import com.bookingBirthday.bookingbirthdayforkids.repository.ThemeRepository;
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

    @Override
    public ResponseEntity<ResponseObj> getAll() {
        try {
            List<Theme> themeList = themeRepository.findAll();
            if(themeList.isEmpty()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "List is empty", null));
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", themeList));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> getById(Long id) {
        try {
            Optional<Theme> theme = themeRepository.findById(id);
            if(theme.isPresent()){
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", theme));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This theme does not exist", null));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> create(MultipartFile imgFile, String themeName, String themDescription) {
        if(themeRepository.existsByThemeName(themeName)){
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(new ResponseObj(HttpStatus.ALREADY_REPORTED.toString(),"Theme name has already exist", null));
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

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Image is invalid", null));

        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Create successful", theme));
    }

    @Override
    public ResponseEntity<ResponseObj> update(Long id, ThemeRequest themeRequest) {
        try {
            Optional<Theme> existTheme = themeRepository.findById(id);
            if (existTheme.isPresent()){
                existTheme.get().setThemeName(themeRequest.getThemeName() == null ? existTheme.get().getThemeName() : themeRequest.getThemeName());
                existTheme.get().setThemeDescription(themeRequest.getThemDescription() == null ? existTheme.get().getThemeDescription() : themeRequest.getThemDescription());
                existTheme.get().setThemeImgUrl(themeRequest.getThemeImgUrl() == null ? existTheme.get().getThemeImgUrl() : themeRequest.getThemeImgUrl());
                existTheme.get().setUpdateAt(LocalDateTime.now());
                themeRepository.save(existTheme.get());
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Update successful", existTheme));
            }
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This theme does not exist", null));

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> delete(Long id)
    {
        try {
            Optional<Theme> theme = themeRepository.findById(id);
            if (theme.isPresent()){
                theme.get().setActive(false);
                theme.get().setDeleteAt(LocalDateTime.now());
                themeRepository.save(theme.get());
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Delete successful", null));
            } else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This theme does not exist", null));

        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }
}

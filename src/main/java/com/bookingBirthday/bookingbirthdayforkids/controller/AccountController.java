package com.bookingBirthday.bookingbirthdayforkids.controller;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.LoginRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.dto.request.AccountRequest;
import com.bookingBirthday.bookingbirthdayforkids.service.AccountService;
import com.bookingBirthday.bookingbirthdayforkids.util.JavaMail;
import com.google.firebase.auth.FirebaseAuthException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/account")
@EnableMethodSecurity(securedEnabled = true)
@CrossOrigin(origins = {"http://Localhost:3000", "https://localhost:8080"}, allowCredentials = "true")
public class AccountController {
    @Autowired
    AccountService accountService;

    @Autowired
    JavaMail javaMail;


    @GetMapping("/get-all")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HOST')")
    public ResponseEntity<ResponseObj> getAll(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "5") int size){
        return accountService.getAll(page, size);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HOST')")
    @GetMapping("/get-id/{id}")
    public ResponseEntity<ResponseObj> getByid(@PathVariable Long id){
        return accountService.getById(id);
    }

    @PostMapping("/register")
    public ResponseEntity<?> create(@Valid @RequestBody AccountRequest accountRequest, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            List<String> errorMessages = new ArrayList<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMessages.add(error.getDefaultMessage());
            }
            return new ResponseEntity<>(errorMessages, HttpStatus.BAD_REQUEST);
        }
        return accountService.create(accountRequest);
    }

    @PostMapping("/signin/gmail")
    public ResponseEntity<?> loginWithGmail(@RequestParam String accessToken) throws FirebaseAuthException {
        return accountService.loginWithGmail(accessToken);
    }

    @PostMapping("/customer/authenticate")
    public ResponseEntity<ResponseObj> authenticate(
            @RequestBody LoginRequest loginRequest
    ){
        return accountService.authenticate(loginRequest);
    }

    @PostMapping("/admin/authenticate")
    public ResponseEntity<ResponseObj> authenticateAdmin(
            @RequestBody LoginRequest loginRequest
    ){
        return accountService.authenticateAdmin(loginRequest);
    }

    @PostMapping("/host/authenticate")
    public ResponseEntity<ResponseObj> authenticateHost(
            @RequestBody LoginRequest loginRequest
    ){
        return accountService.authenticateHost(loginRequest);
    }

    @GetMapping("/information")
    public ResponseEntity<?> test(){
        return accountService.information();
    }

    @GetMapping("/test1")
    public ResponseEntity<?> test1(){
        javaMail.sendEmail("tranquoccuong0179@gmail.com", "123", "123");
        return ResponseEntity.ok().body(javaMail);
    }

    @PutMapping(value = "/update-img", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseObj> updateImg(@RequestPart(name = "fileImg", required = true)MultipartFile fileImg){
        return accountService.updateImg(fileImg);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseObj> update(@PathVariable Long id, @RequestBody AccountRequest accountRequest){
        return accountService.updateAccount(id, accountRequest);
    }
//
//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<ResponseObj> delete(@PathVariable Long id){
//        return accountService.delete(id);
//    }

}

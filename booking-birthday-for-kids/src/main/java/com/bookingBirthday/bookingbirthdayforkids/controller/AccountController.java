package com.bookingBirthday.bookingbirthdayforkids.controller;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.LoginRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.AuthenticationResponse;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.model.Account;
import com.bookingBirthday.bookingbirthdayforkids.dto.request.AccountRequest;
import com.bookingBirthday.bookingbirthdayforkids.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/account")
@EnableMethodSecurity(securedEnabled = true)
@CrossOrigin(origins = "http://Localhost:3000")
public class AccountController {
    @Autowired
    AccountService accountService;


    @GetMapping("/get-all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseObj> getAll(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "5") int size){
        return accountService.getAll(page, size);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
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

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody LoginRequest loginRequest
    ){
        return accountService.authenticate(loginRequest);
    }

    @GetMapping("/test")
    public ResponseEntity<?> test(){
        return ResponseEntity.ok((Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }
//
//    @PutMapping("/update/{id}")
//    public ResponseEntity<ResponseObj> update(@PathVariable Long id, @RequestBody AccountRequest accountRequest){
//        return accountService.update(id, accountRequest);
//    }
//
//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<ResponseObj> delete(@PathVariable Long id){
//        return accountService.delete(id);
//    }

}

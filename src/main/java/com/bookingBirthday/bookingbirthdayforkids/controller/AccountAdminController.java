package com.bookingBirthday.bookingbirthdayforkids.controller;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.AccountRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.service.AccountAdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/account/admin")
public class AccountAdminController {
    @Autowired
    AccountAdminService accountAdminService;

    @GetMapping("/get-all/customer")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseObj> getAllCustomer(){
        return accountAdminService.getAllCustomer();
    }

    @GetMapping("/get-all/host")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseObj> getAllHost(){
        return accountAdminService.getAllHost();
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('HOST')")
    @GetMapping("/get-id/{id}")
    public ResponseEntity<ResponseObj> getByid(@PathVariable Long id){
        return accountAdminService.getById(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<?> create(@Valid @RequestBody AccountRequest accountRequest) {
        return accountAdminService.create(accountRequest);
    }
    @GetMapping("/information")
    public ResponseEntity<?> test(){
        return accountAdminService.information();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseObj> delete(@RequestParam Long id){
        return accountAdminService.delete(id);
    }
}

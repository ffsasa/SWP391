package com.bookingBirthday.bookingbirthdayforkids.service.impl;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.AccountRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.RegisterResponse;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.model.Account;
import com.bookingBirthday.bookingbirthdayforkids.model.Role;
import com.bookingBirthday.bookingbirthdayforkids.model.RoleEnum;
import com.bookingBirthday.bookingbirthdayforkids.repository.AccountRepository;
import com.bookingBirthday.bookingbirthdayforkids.repository.RoleRepository;
import com.bookingBirthday.bookingbirthdayforkids.service.AccountAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AccountAdminServiceImpl implements AccountAdminService {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<ResponseObj> getAllCustomer() {
        List<Account> accountPage = accountRepository.findAllByRoleId(3L);
        if (accountPage.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "List is empty", null));

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.OK.toString(), "OK", accountPage));

    }

    @Override
    public ResponseEntity<ResponseObj> getAllHost() {
        List<Account> accountPage = accountRepository.findAllByRoleId(2L);
        if (accountPage.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "List is empty", null));

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.OK.toString(), "OK", accountPage));

    }

    @Override
    public ResponseEntity<ResponseObj> getById(Long id) {
        try {
            Optional<Account> account = accountRepository.findById(id);
            if(account.isPresent())
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), null, account));
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Account does not exist", null));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<?> create(AccountRequest accountRequest) {
        if (accountRepository.existsAccountByEmailAndUsername(accountRequest.getEmail(), accountRequest.getUsername()))
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(new ResponseObj(HttpStatus.ALREADY_REPORTED.toString(), "Email and UserName has already", null));

        else if(accountRepository.existsAccountByEmail(accountRequest.getEmail()))
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(new ResponseObj(HttpStatus.ALREADY_REPORTED.toString(), "Email has already", null));

        else if(accountRepository.existsAccountByEmail(accountRequest.getUsername()))
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(new ResponseObj(HttpStatus.ALREADY_REPORTED.toString(), "UserName has already", null));

        Account account = new Account();
        Role role = roleRepository.findByName(RoleEnum.HOST);
        account.setUsername(accountRequest.getUsername());
        account.setEmail(accountRequest.getEmail());
        account.setFullName(accountRequest.getFullName());
        account.setDob(accountRequest.getDob());
        account.setPassword(passwordEncoder.encode(accountRequest.getPassword()));
        account.setPhone(accountRequest.getPhone());
        account.setActive(true);
        account.setCreateAt(LocalDateTime.now());
        account.setUpdateAt(LocalDateTime.now());
        account.setRole(role);

        accountRepository.save(account);
        RegisterResponse accountResponse = new RegisterResponse();
        accountResponse.setId(account.getId());
        accountResponse.setUsername(account.getUsername());
        accountResponse.setFullName(account.getFullName());
        return ResponseEntity.ok().body(accountResponse);
    }

    @Override
    public ResponseEntity<ResponseObj> information() {
        Account account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "200", account));
    }

    @Override
    public ResponseEntity<ResponseObj> delete(Long id) {
        Optional<Account> account = accountRepository.findById(id);
        if(account.isPresent()){
            account.get().setActive(false);
            accountRepository.save(account.get());
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Delete successful", account));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This account does not exist", null));
    }
}

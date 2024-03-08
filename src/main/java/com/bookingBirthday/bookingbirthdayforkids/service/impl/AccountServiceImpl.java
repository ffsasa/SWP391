package com.bookingBirthday.bookingbirthdayforkids.service.impl;

import com.bookingBirthday.bookingbirthdayforkids.config.JwtService;
import com.bookingBirthday.bookingbirthdayforkids.dto.request.LoginRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.AuthenticationResponse;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.RegisterResponse;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.model.Account;
import com.bookingBirthday.bookingbirthdayforkids.model.Role;
import com.bookingBirthday.bookingbirthdayforkids.model.RoleEnum;
import com.bookingBirthday.bookingbirthdayforkids.repository.AccountRepository;
import com.bookingBirthday.bookingbirthdayforkids.dto.request.AccountRequest;
import com.bookingBirthday.bookingbirthdayforkids.repository.RoleRepository;
import com.bookingBirthday.bookingbirthdayforkids.service.AccountService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtService jwtService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    RoleRepository roleRepository;
    @Override
    public ResponseEntity<ResponseObj> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Account> accountPage = accountRepository.findAll(pageable);
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
        Role role = roleRepository.findByName(RoleEnum.CUSTOMER);
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
//        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Create successful", account));
    }

    @Override
    public ResponseEntity<ResponseObj> authenticate(LoginRequest request) {
        Role role = roleRepository.findByName(RoleEnum.CUSTOMER);
        Authentication authentication;
        authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        Account account = (Account) authentication.getPrincipal();
        if(!role.equals(account.getRole())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "You are not permisson", null));
        }
//
//        var user = accountRepository.findByUsername(request.getUsername())
//                .orElseThrow(() ->new RuntimeException("User Not Found"));
        var jwtToken = jwtService.generateToken(account);
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setToken(jwtToken);
        authenticationResponse.setRole(role);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Login success", authenticationResponse));
    }

    @Override
    public ResponseEntity<ResponseObj> authenticateAdmin(LoginRequest loginRequest) {
        Role role = roleRepository.findByName(RoleEnum.ADMIN);
        Authentication authentication;
        authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        Account account = (Account) authentication.getPrincipal();
        if(!role.equals(account.getRole())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "You are not permisson", null));
        }
//
//        var user = accountRepository.findByUsername(request.getUsername())
//                .orElseThrow(() ->new RuntimeException("User Not Found"));
        var jwtToken = jwtService.generateToken(account);
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setToken(jwtToken);
        authenticationResponse.setRole(role);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Login success", authenticationResponse));
    }

    @Override
    public ResponseEntity<ResponseObj> authenticateHost(LoginRequest loginRequest) {
        Role role = roleRepository.findByName(RoleEnum.HOST);
        Authentication authentication;
        authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        Account account = (Account) authentication.getPrincipal();
        if(!role.equals(account.getRole())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "You are not permisson", null));
        }
//
//        var user = accountRepository.findByUsername(request.getUsername())
//                .orElseThrow(() ->new RuntimeException("User Not Found"));
        var jwtToken = jwtService.generateToken(account);
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setToken(jwtToken);
        authenticationResponse.setRole(role);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Login success", authenticationResponse));
    }

    public ResponseEntity<?> loginWithGmail(String accessToken) throws FirebaseAuthException {
        Role role = roleRepository.findByName(RoleEnum.CUSTOMER);
        FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(accessToken);
        if(accountRepository.findByEmail(decodedToken.getEmail()).isPresent()){
//            authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(
//                            decodedToken.getEmail(),
//                            null
//                    )
//            );

            var user = accountRepository.findByEmail(decodedToken.getEmail())
                    .orElseThrow(() ->new RuntimeException("Email Not Found"));
            var jwtToken = jwtService.generateToken(user);
            AuthenticationResponse authenticationResponse = new AuthenticationResponse();
            authenticationResponse.setToken(jwtToken);


            return ResponseEntity.status(HttpStatus.OK).body(authenticationResponse);
        }
        else {
            Account account = new Account();
            account.setFullName(decodedToken.getName());
            account.setEmail(decodedToken.getEmail());
            account.setUsername(decodedToken.getEmail());
            account.setAvatarUrl(decodedToken.getPicture());
            account.setActive(true);
            account.setRole(role);
            account.setCreateAt(LocalDateTime.now());
            account.setUpdateAt(LocalDateTime.now());
            accountRepository.save(account);

//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        decodedToken.getEmail(),
//                        null
//                )
//        );

            var user = accountRepository.findByEmail(decodedToken.getEmail())
                    .orElseThrow(() ->new RuntimeException("Email Not Found"));
            var jwtToken = jwtService.generateToken(user);
            AuthenticationResponse authenticationResponse = new AuthenticationResponse();
            authenticationResponse.setToken(jwtToken);

            return ResponseEntity.status(HttpStatus.OK).body(authenticationResponse);
        }

    }

    @Override
    public ResponseEntity<ResponseObj> information() {
        Account account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "200", account));

    }


    //    @Override
//    public ResponseEntity<ResponseObj> update(Long id, AccountRequest accountRequest) {
////        Account existAccount = accountRepository.findById(id).orElseThrow();
//        Optional<Account> existAccount = accountRepository.findById(id);
//
//        if (existAccount.isPresent()){
//            existAccount.get().setUsername(accountRequest.getUsername() == null ? existAccount.get().getUsername() : accountRequest.getUsername());
//            existAccount.get().setFullName(accountRequest.getFullName() == null ? existAccount.get().getFullName() : accountRequest.getFullName());
//            existAccount.get().setEmail(accountRequest.getEmail() == null ? existAccount.get().getEmail() : accountRequest.getEmail());
//            existAccount.get().setPassword(accountRequest.getPassword() == null ? existAccount.get().getPassword() : accountRequest.getPassword());
//            existAccount.get().setPhone(accountRequest.getPhone() == null ? existAccount.get().getPhone() : accountRequest.getPhone());
//            existAccount.get().setAvatarUrl(accountRequest.getAvatarUrl() == null ? existAccount.get().getAvatarUrl() : accountRequest.getAvatarUrl());
//            existAccount.get().setUpdateAt(LocalDateTime.now());
//            accountRepository.save(existAccount.get());
//            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Update successful", existAccount));
//        }
//        else
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Account does not exist", null));
//
//    }

//    @Override
//    public ResponseEntity<ResponseObj> delete(Long id) {
//        Optional<Account> account = accountRepository.findById(id);
//        if (account.isPresent()){
//            account.get().setActive(false);
//            account.get().setUpdateAt(LocalDateTime.now());
//            accountRepository.save(account.get());
//            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Delete successful", null));
//        }
//        else
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Account does not exist", null));
//    }


}

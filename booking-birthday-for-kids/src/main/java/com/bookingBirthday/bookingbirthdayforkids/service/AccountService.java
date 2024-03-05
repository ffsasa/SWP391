package com.bookingBirthday.bookingbirthdayforkids.service;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.AccountRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.request.LoginRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.AuthenticationResponse;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.http.ResponseEntity;

public interface AccountService {
    public ResponseEntity<ResponseObj> getAll(int page, int size);

    public ResponseEntity<ResponseObj> getById(Long id);

    public ResponseEntity<?> create(AccountRequest accountRequest);

    public ResponseEntity<AuthenticationResponse> authenticate(LoginRequest request);
    public ResponseEntity<?> loginWithGmail(String accessToken) throws FirebaseAuthException;

}

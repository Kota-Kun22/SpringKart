package org.example.springkart.project.service;

import jakarta.validation.Valid;
import org.example.springkart.project.payload.AuthenticationResult;
import org.example.springkart.project.payload.UserResponse;
import org.example.springkart.project.security.request.LoginRequest;
import org.example.springkart.project.security.request.SignupRequest;
import org.example.springkart.project.security.resposne.MessageResponse;
import org.example.springkart.project.security.resposne.UserInfoResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface AuthService {


    AuthenticationResult Sign(LoginRequest loginRequest);

    ResponseEntity<MessageResponse> SignUp(@Valid SignupRequest signupRequest);


    UserInfoResponse getCurrentUserDetails(Authentication authentication);

    ResponseCookie SignOut();

    String currentUserName(Authentication authentication);

    UserResponse getAllSellers(Pageable pageDetails);
}

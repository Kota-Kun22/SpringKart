package org.example.springkart.project.Controller;


import jakarta.validation.Valid;
import org.example.springkart.project.config.AppConstants;
import org.example.springkart.project.model.AppRole;
import org.example.springkart.project.model.Role;
import org.example.springkart.project.model.User;
import org.example.springkart.project.payload.AuthenticationResult;
import org.example.springkart.project.payload.UserResponse;
import org.example.springkart.project.repository.RoleRepository;
import org.example.springkart.project.repository.UserRepository;
import org.example.springkart.project.security.jwt.JwtUtils;
import org.example.springkart.project.security.request.LoginRequest;
import org.example.springkart.project.security.request.SignupRequest;
import org.example.springkart.project.security.resposne.MessageResponse;
import org.example.springkart.project.security.resposne.UserInfoResponse;
import org.example.springkart.project.security.services.UserDetailsImpl;
import org.example.springkart.project.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest ){

        AuthenticationResult result = authService.Sign(loginRequest);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE,
                        result.getJwtCookie().toString())
                .body(result.getResponse());

    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest){

        return authService.SignUp(signupRequest);
    }

    @GetMapping("/user/me")
    public String currentUserName(Authentication authentication)
    {
        String userName = authService.currentUserName(authentication);
        return userName;
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserDetails(Authentication authentication)
    {
        UserInfoResponse response= authService.getCurrentUserDetails(authentication);
        return ResponseEntity.ok().body(response);

    }

    @PostMapping("/signout")
    public ResponseEntity<?> signout(Authentication authentication)
    {
        ResponseCookie cookie= authService.SignOut();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE,cookie.toString())
                .body(new MessageResponse("Successfully signed out"));
    }

    @GetMapping("/sellers")
    public ResponseEntity<UserResponse> getAllSellers(
            @RequestParam (name= "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false)Integer pageNumber){

        Sort sortByAndOrder = Sort.by(AppConstants.SORT_USERS_BY).descending();
        Pageable pageDetails= PageRequest.of(pageNumber,Integer.parseInt(AppConstants.PAGE_SIZE),sortByAndOrder);
        return ResponseEntity.ok(authService.getAllSellers(pageDetails));

    }


}

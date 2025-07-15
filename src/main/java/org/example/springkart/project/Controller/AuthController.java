package org.example.springkart.project.Controller;


import jakarta.validation.Valid;
import org.example.springkart.project.model.AppRole;
import org.example.springkart.project.model.Role;
import org.example.springkart.project.model.User;
import org.example.springkart.project.repository.RoleRepository;
import org.example.springkart.project.repository.UserRepository;
import org.example.springkart.project.security.jwt.JwtUtils;
import org.example.springkart.project.security.request.LoginRequest;
import org.example.springkart.project.security.request.SignupRequest;
import org.example.springkart.project.security.resposne.MessageResponse;
import org.example.springkart.project.security.resposne.UserInfoResponse;
import org.example.springkart.project.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
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
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest ){

        Authentication authentication;
        try{
            authentication= authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
        }catch(AuthenticationException exception)
        {
            Map<String,Object> map = new HashMap<>();
            map.put("message","BAD CREDENTIALS");
            map.put("STATUS",false);
            return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);//saving the authenticated use in the spring context holder
        //now generating the token

        UserDetailsImpl userDetails= (UserDetailsImpl) authentication.getPrincipal();

        ResponseCookie JwtCookie = jwtUtils.generateJwtCookie(userDetails);

        List<String> role= userDetails.getAuthorities().stream()
                .map(item ->item.getAuthority())
                .collect(Collectors.toList());
        System.out.println("signin request: " + loginRequest);

        UserInfoResponse resposne = new UserInfoResponse(
                userDetails.getId(),
                userDetails.getUsername(),
                role,
                jwtUtils.generateTokenFromUserName(userDetails.getUsername())
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE,
                JwtCookie.toString()).body(resposne);

    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest){

        if(userRepository.existsByUserName(signupRequest.getUsername()))
        {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error : UserName is Already taken !! try another one"));
        }

        if(userRepository.existsByEmail(signupRequest.getEmail()))
        {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error : Email is Already taken !! try another one"));
        }

        User user= new User(
                signupRequest.getUsername(),
                signupRequest.getEmail(),
                passwordEncoder.encode(signupRequest.getPassword())
        );
        Set<String> strRoles= signupRequest.getRoles();
        Set<Role> roles= new HashSet<>();//creating empty set

        if(strRoles==null){
            //where user have'nt send anything giving him a default one
            Role userRole= roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseThrow(()-> new RuntimeException("Error :ROle is not find"));
            roles.add(userRole);
        }else {
            //here we are converting what user send into what our database understands
            //user typed admin ---- converting it ----> ROLE_ADMIN
            strRoles.forEach(role ->{
                switch (role){

                    case "admin":
                        Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                                .orElseThrow(()-> new RuntimeException("Error :ROle is not find"));
                        roles.add(adminRole);
                        break;
                    case "seller":
                        Role sellerRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER)
                                .orElseThrow(()-> new RuntimeException("Error :ROle is not find"));
                        roles.add(sellerRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                                .orElseThrow(()-> new RuntimeException("Error :ROle is not find"));
                        roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }

    @GetMapping("/user/me")
    public String currentUserName(Authentication authentication)
    {
        if(authentication!=null)
        {
            return authentication.getName();
        }else{
            return " ";
        }
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserDetails(Authentication authentication)
    {
        UserDetailsImpl userDetails= (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles= userDetails.getAuthorities().stream()
                .map(item ->item.getAuthority())
                .collect(Collectors.toList());

        UserInfoResponse resposne = new UserInfoResponse(userDetails.getId(),userDetails.getUsername(),roles);

        return ResponseEntity.ok().body(resposne);

    }

    @PostMapping("/signout")
    public ResponseEntity<?> signout(Authentication authentication)
    {
        ResponseCookie cookie= jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE,cookie.toString())
                .body(new MessageResponse("Successfully signed out"));
    }


}

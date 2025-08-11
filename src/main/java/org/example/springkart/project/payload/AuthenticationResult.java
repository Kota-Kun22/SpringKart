package org.example.springkart.project.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.springkart.project.security.resposne.UserInfoResponse;
import org.springframework.http.ResponseCookie;

@Data
@AllArgsConstructor
public class AuthenticationResult {
    private final UserInfoResponse response;
    private final ResponseCookie JwtCookie;
}
//i have made this so that i can get the both response and cookie at the same time
// plus it also handle the communication between the controller and the service

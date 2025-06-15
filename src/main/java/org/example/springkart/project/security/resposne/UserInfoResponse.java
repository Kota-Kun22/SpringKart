package org.example.springkart.project.security.resposne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class UserInfoResponse {

    private Long id;
    private String token;

    private String username;
    private List<String> roles;

    public UserInfoResponse(Long id, String token, String username, List<String> roles) {
        this.id = id;
        this.token = token;
        this.username = username;
        this.roles = roles;
    }

    public UserInfoResponse(Long id, String username, List<String> role) {
        this.id = id;
        //this.token = token;
        this.roles = role;
        this.username = username;
    }
}

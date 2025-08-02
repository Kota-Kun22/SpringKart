package org.example.springkart.project.security.resposne;

import com.fasterxml.jackson.annotation.JsonInclude;
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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String jwtToken;

    private String username;
    private List<String> roles;
    private String email;

    public UserInfoResponse(Long id, String username, List<String> roles, String email ,String jwtToken) {
        this.id = id;
        this.username = username;
        this.roles = roles;
        this.email = email;
        this.jwtToken = jwtToken;
    }

    public UserInfoResponse(Long id, String username, List<String> role) {
        this.id = id;
        this.roles = role;
        this.username = username;
    }


}

package org.example.springkart.project.security.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.stereotype.Service;

import java.util.Set;


@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {

    @NotBlank
    @Size(min = 3,max = 20)
    private String username ;

    @Email
    @NotBlank
    @Size(min = 5,max = 50)
    private String email ;

    private Set<String> roles ;

    @NotBlank
    @Size(min = 6,max = 120)
    private String password;



    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}

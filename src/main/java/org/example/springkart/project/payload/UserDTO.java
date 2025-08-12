package org.example.springkart.project.payload;

import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.springkart.project.model.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long userId;
    private String username;
    private String email;
    private String password;
    private Set<Role> roles = new HashSet<>();
    private AddressDTO address;
    private CartDTO cart;
}
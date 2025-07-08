package org.example.springkart.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(name = "users",
        uniqueConstraints ={
                @UniqueConstraint(columnNames = "email"),
                @UniqueConstraint(columnNames = "username")})
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long userId;

    @NotBlank
    @Size(min = 3,max= 20)
    @Column(name="username")
    private String userName;

    @Email
    @NotBlank
    @Size(min = 5,max= 50)
    @Column(name="email")
    private String email;

    @NotBlank
    @Size(min = 6,max= 120)
    @Column(name="password")
    private String password;

    public User(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    @Getter
    @Setter
    @ManyToMany(fetch = FetchType.EAGER,cascade= {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();


    @Getter
    @Setter
    @ManyToMany(cascade={CascadeType.MERGE,CascadeType.PERSIST},fetch = FetchType.EAGER)
    @JoinTable(name= "user_address",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "address_id")
    )
    private List<Address> addresses= new ArrayList<>();


    @ToString.Exclude
    @OneToMany(mappedBy = "user",
            cascade = {CascadeType.MERGE,CascadeType.PERSIST},
            orphanRemoval = true )
    private Set<Product> products;



    @ToString.Exclude
    @OneToOne(mappedBy = "user",cascade = {CascadeType.MERGE,CascadeType.PERSIST},orphanRemoval = true)
    private Cart cart;


}

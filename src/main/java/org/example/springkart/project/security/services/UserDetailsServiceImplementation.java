package org.example.springkart.project.security.services;

import jakarta.transaction.Transactional;
import org.example.springkart.project.model.User;
import org.example.springkart.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImplementation implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user= userRepository.findByUserName(username)
                .orElseThrow(()-> new UsernameNotFoundException("User not Found with this name"+username));
        // it is like we are fetching the data from the database and it will in which type YES that will be our's User model
        //but spring sec needs its userDetails
        return UserDetailsImpl.build(user) ;
    }
}

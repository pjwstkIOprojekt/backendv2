package com.gary.backendv2.security.service;


import com.gary.backendv2.model.users.User;
import com.gary.backendv2.model.security.UserPrincipal;
import com.gary.backendv2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = Optional.ofNullable(userRepository.getByEmail(username));
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException(String.format("Username %s not found", username));
        }

        return UserPrincipal.of(userOptional.get());
    }
}

package com.implementation.JournalApp.security.UserServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.implementation.JournalApp.entity.UserEntity;
import com.implementation.JournalApp.repository.UserRepository;

// import com.implementation.JournalApp.security.UserServices.CustomUserDetails;

@Service
public class CustomUserDetailsService implements UserDetailsService {

        @Autowired
        private UserRepository userRepository;

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                UserEntity userEntity = userRepository.findByusername(username);
                if (userEntity == null) {
                        throw new UsernameNotFoundException("User not found");
                }
                return new CustomUserDetails(userEntity);
        }
}
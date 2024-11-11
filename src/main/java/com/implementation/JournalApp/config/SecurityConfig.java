package com.implementation.JournalApp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
// import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.crypto.password.NoOpPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import com.implementation.JournalApp.security.filters.CustomSessionAuthenticationFilter;
// import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
public class SecurityConfig {

        @Autowired
        private CustomSessionAuthenticationFilter customSessionAuthenticationFilter;

        @Bean
        @Order(1)
        public SecurityFilterChain publicSecurityFilterChain(HttpSecurity http) throws Exception { // For formatting
                return http.securityMatcher("/api/user/login", "/api/user/register", "/api/user/logout") // For formatting
                                .authorizeHttpRequests(authorizeRequests -> authorizeRequests.anyRequest().permitAll()) // For formatting
                                .csrf(csrf -> csrf.disable()) // For formatting
                                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // For formatting
                                .httpBasic(basic -> basic.disable()) // For formatting
                                .build(); // For formatting
        }

        @Bean
        @Order(2)
        public SecurityFilterChain protectedSecurityFilterChain(HttpSecurity http) throws Exception { // For formatting
                return http.authorizeHttpRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated()) // For formatting
                                .csrf(csrf -> csrf.disable()) // For formatting
                                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // For formatting
                                .addFilterAt(customSessionAuthenticationFilter, BasicAuthenticationFilter.class) // For formatting
                                .build(); // For formatting
        }
}
package com.implementation.JournalApp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import com.implementation.JournalApp.security.filters.CustomSessionAuthenticationFilter;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
public class SecurityConfig {

        @Autowired
        private CustomSessionAuthenticationFilter customSessionAuthenticationFilter;

        @Autowired
        private UserDetailsService userDetailsService;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                return http.authorizeHttpRequests(authorizeRequests -> authorizeRequests // comment for formatting
                                .requestMatchers("/api/user/login").permitAll() // comment for formatting
                                .requestMatchers("/api/user/register").permitAll() // comment for formatting
                                .requestMatchers("/api/user/logout").permitAll() // comment for formatting
                                .anyRequest().authenticated()) // comment for formatting
                                .formLogin(formLogin -> formLogin.disable()) // comment for formatting
                                .csrf(csrf -> csrf.disable()) // comment for formatting
                                .httpBasic(withDefaults()) // comment for formatting
                                .logout(logout -> logout.disable()) // comment for formatting
                                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // comment for formatting
                                .addFilterBefore(customSessionAuthenticationFilter, BasicAuthenticationFilter.class) // comment for formatting
                                .build(); // comment for formatting
        }

        @Bean
        public DaoAuthenticationProvider authenticationProvider() {
                DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
                authProvider.setUserDetailsService(userDetailsService);
                authProvider.setPasswordEncoder(passwordEncoder());
                return authProvider;
        }

        @SuppressWarnings("deprecation")
        @Bean
        public PasswordEncoder passwordEncoder() {
                return NoOpPasswordEncoder.getInstance();
        }
}
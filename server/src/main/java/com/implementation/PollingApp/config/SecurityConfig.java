package com.implementation.PollingApp.config;

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
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// import com.implementation.JournalApp.security.UserServices.CustomUserDetailsService;
import com.implementation.PollingApp.security.filters.CustomSessionAuthenticationFilter;

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
                return http.securityMatcher("/api/user/login", "/api/user/register", "/api/user/logout", "/api/user/verifyUser") // For formatting
                                .cors().and() // For formatting
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
                                .cors().and() // For formatting
                                .csrf(csrf -> csrf.disable()) // For formatting
                                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // For formatting
                                .addFilterAt(customSessionAuthenticationFilter, BasicAuthenticationFilter.class) // For formatting
                                .httpBasic(basic -> basic.disable()) // For formatting
                                .build(); // For formatting
        }

        @Bean
        @Order(3)
        public WebMvcConfigurer corsConfigurer() {
                return new WebMvcConfigurer() {
                        @Override
                        public void addCorsMappings(CorsRegistry registry) {
                                registry.addMapping("/**")// for formatting
                                                .allowedOrigins("http://localhost:5173", "http://localhost:3000") // Add localhost:3000 here
                                                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // for formatting
                                                .allowedHeaders("*")// for formatting
                                                .allowCredentials(true);
                        }
                };
        }

        // adding userDetailsService
        // @Bean
        // public UserDetailsService userDetailsService() {
        // return new CustomUserDetailsService();
        // }
}
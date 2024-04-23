//package com.nwamara.studentportal.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
////    @Bean
////    public SecurityFilterChain securityWebFilterChain(HttpSecurity http) {
////        http
////                .authorizeHttpRequests((authz) -> authz
////                        .anyRequest().authenticated()
////                )
////                .httpBasic(withDefaults());
////        return http.build();
////    }
//
////    @Bean
////    public WebSecurityCustomizer webSecurityCustomizer() throws Exception {
////        return (web) -> web.ignoring().an
////    }
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//
//}

package com.springBoot.board.security;

import com.springBoot.board.jwt.JwtAuthenticationFilter;
import com.springBoot.board.jwt.JwtTokenProvider;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // 웹 브라우저 위의 웹 페이지에서 동작하는 REST API에서는 동일 근원 정책에의해 보호되므로 csrf를 사용하지않는다.
                // 위 설정을 하지않으면 post 요청 에서 403 forbidden 발생
                .authorizeHttpRequests((authorize) -> authorize
//                        .requestMatchers("/login").permitAll()
//                        .requestMatchers("/admin/**").hasRole("ADMIN") // 관리자 페이지가 있으면 쓸만할듯
                        .anyRequest().permitAll()
                        .and()
                        .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // Spring Security는 UserDetailsService를 구현하여 메모리에 저장된 사용자 이름 / 암호 기반 인증을 지원한다.
        UserDetails userDetails = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
//                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(userDetails);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() { // 비밀번호 암호화
        return new BCryptPasswordEncoder();
    }
}
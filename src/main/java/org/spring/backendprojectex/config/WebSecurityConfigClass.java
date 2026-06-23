package org.spring.backendprojectex.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfigClass {

    // 비밀번호 암호화
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    // 시큐리티 로그인실패
    @Bean
    public CustomAuthenticationFailerHandler customAuthenticationFailerHandler() {
        return new CustomAuthenticationFailerHandler();
    }
    // 시큐리티 로그인성공
    @Bean
    public CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }
    // 시큐리티 로그아웃 성공
    public CustomLogoutSuccessHandler customLogoutSuccessHandler() {
        return new CustomLogoutSuccessHandler();
    }

    // 시큐리티 설정
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                  MyDefaultOAuth2UserService myDefaultOAuth2UserService) throws Exception {
        // 1. csrf 설정   (람다식)
        http.csrf(AbstractHttpConfigurer::disable);
        // 2. 사용자 별 페이지 설정
        http.authorizeHttpRequests(authorize->
            authorize
                    .requestMatchers("/member/join","/member/login").permitAll()            // 모든 접근 허용
                    .requestMatchers("/member/logout","/member/detail").authenticated()     // 로그인 후
                    .requestMatchers("/admin/**").hasRole("ADMIN")                     // 관리자 권한
                    .requestMatchers("/shop/insert").hasRole("ADMIN")
                    .requestMatchers("/cart/**").authenticated()
                    .requestMatchers("/payment/**").authenticated()
                    .requestMatchers("/ws/**","/chatEndpoint/**").permitAll()                                  // 웹소켓 엔드포인트 허용
                    .requestMatchers("/community/write","/community/write/**").authenticated()
                    .anyRequest().permitAll()                                                       // 나머지 모두 허용
        );
        // 3. 로그인
        http.formLogin(login->
            login.loginPage("/member/login")                         // 로그인 페이지
                    .usernameParameter("userEmail")           // 로그인 아이디
                    .passwordParameter("userPw")              // 로그인 비밀번호
                    .loginProcessingUrl("/member/login")       // 로그인 POST
                    .successHandler(customAuthenticationSuccessHandler())
                    .failureHandler(customAuthenticationFailerHandler())
//                    .defaultSuccessUrl("/")                     // 로그인 성공시
//                    .failureUrl("/")                        // 로그인 실패시
                    .permitAll()
        );
        // 4. 로그아웃
        http.logout(logout->
            logout.logoutUrl("/member/logout")
                    .logoutSuccessHandler(customLogoutSuccessHandler())
//                    .logoutSuccessUrl("/")
                    .permitAll()
        );
        // 5. OAuth2.0 로그인설정
        http.oauth2Login(oauth2 ->
                oauth2
                        .loginPage("/member/login")
                        .userInfoEndpoint(userInfo ->
                                userInfo
                                        .userService(myDefaultOAuth2UserService)
                        )
        );
        return http.build();
    }
}

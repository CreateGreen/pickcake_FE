package co.pickcake.config;



import co.pickcake.authdomain.handler.AuthSuccessHandler;
import co.pickcake.authdomain.handler.Http401Handler;
import co.pickcake.authdomain.handler.Http403Handler;
import co.pickcake.authdomain.handler.AuthFailHandler;
import co.pickcake.authdomain.repository.MemberRepository;
import co.pickcake.authdomain.service.AuthDetailsService;

import co.pickcake.config.filter.EmailPasswordAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;


import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.session.security.web.authentication.SpringSessionRememberMeServices;


import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final ObjectMapper objectMapper;
    private final MemberRepository memberRepository;
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
//                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .requestMatchers("/favicon.ico")
                .requestMatchers(toH2Console())
                ;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
//                        .requestMatchers("/").permitAll()
//                        .requestMatchers("*?error").permitAll()
//                        .requestMatchers("/auth/signup").permitAll()
//                        .requestMatchers( "/auth/signin").permitAll()
//                        .requestMatchers("/images/**").permitAll()
//                        .requestMatchers( "/api","/api/cake", "/cakes","/cakes/category**","/images/**").hasRole("USER")
//                        .anyRequest().authenticated()
                        .anyRequest().permitAll()
                )
                .addFilterBefore(usernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
//                .formLogin((form) -> form
//                        .loginPage("/auth/signin")
//                        .loginProcessingUrl("/auth/signin")
//                        .usernameParameter("username")
//                        .passwordParameter("password")
//                        .defaultSuccessUrl("/", true)
//                        .failureHandler(new AuthFailHandler(objectMapper))
//                        .successHandler(new AuthSuccessHandler(objectMapper))
//                        .permitAll()
//                )
//                .rememberMe(m -> m.rememberMeParameter("remember-me")
//                        .alwaysRemember(false)
//                        .tokenValiditySeconds(86400)
//                )
                .exceptionHandling(e -> {
                    e.accessDeniedHandler(new Http403Handler(objectMapper));
                    e.authenticationEntryPoint(new Http401Handler(objectMapper));
                })
                .logout(LogoutConfigurer::permitAll)
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public UserDetailsService userDetailsService() {
        return new AuthDetailsService(memberRepository);
    }
    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(provider);
    }
    @Bean
    public EmailPasswordAuthenticationFilter usernamePasswordAuthenticationFilter() {
        EmailPasswordAuthenticationFilter filter = new EmailPasswordAuthenticationFilter("/auth/login", objectMapper);
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(new AuthSuccessHandler(objectMapper));
        filter.setAuthenticationFailureHandler(new AuthFailHandler(objectMapper));
        filter.setSecurityContextRepository(new HttpSessionSecurityContextRepository());
        SpringSessionRememberMeServices rememberMeServices = new SpringSessionRememberMeServices();
        rememberMeServices.setAlwaysRemember(true);
        rememberMeServices.setValiditySeconds(3600);
        filter.setRememberMeServices(rememberMeServices);
        return filter;
    }

}
package com.dailycodework.dreamshops.security.config;

import com.dailycodework.dreamshops.security.jwt.AuthTokenFilter;
import com.dailycodework.dreamshops.security.jwt.JwtAuthEntryPoint;
import com.dailycodework.dreamshops.security.user.ShopUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@EnableWebSecurity
@Configuration
public class ShopConfig {

    @Autowired
    private final ShopUserDetailsService userDetailsService;
    @Autowired
    private final JwtAuthEntryPoint jwtAuthEntryPoint;
    private static final List<String> SECURED_URLS =
            List.of("/api/v1/carts/**", "/api/v1/cartItems/**");

    @Autowired
    public ShopConfig(ShopUserDetailsService userDetailsService, JwtAuthEntryPoint jwtAuthEntryPoint) {

        this.userDetailsService = userDetailsService;
        this.jwtAuthEntryPoint = jwtAuthEntryPoint;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthTokenFilter authTokenFilter() {

        return new AuthTokenFilter();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {

        return authConfig.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {

        var authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth.requestMatchers(SECURED_URLS.toArray(String[]::new)).authenticated()
                        .anyRequest().permitAll());

        http.authenticationProvider(daoAuthenticationProvider());
        //http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(authTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
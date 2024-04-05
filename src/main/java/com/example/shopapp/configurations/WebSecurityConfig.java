package com.example.shopapp.configurations;

import com.example.shopapp.filters.JwtTokenFilter;
import com.example.shopapp.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;
    @Value("${api.prefix}")
    private String apiPrefix;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        http.csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(request->
                {
                    request.requestMatchers(
                            String.format("%s/users/register",apiPrefix),
                            String.format("%s/users/login",apiPrefix)
                            )
                            .permitAll()
                            //category request
                            .requestMatchers(HttpMethod.GET,String.format("%s/categories**",apiPrefix)).hasAnyRole(Role.ADMIN,Role.USER)
                            .requestMatchers(HttpMethod.POST,String.format("%s/categories/**",apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.PUT,String.format("%s/categories/**",apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.DELETE,String.format("%s/categories/**",apiPrefix)).hasRole(Role.ADMIN)
                            //product request
                            .requestMatchers(HttpMethod.GET,String.format("%s/products**",apiPrefix)).hasAnyRole(Role.ADMIN,Role.USER)
                            .requestMatchers(HttpMethod.POST,String.format("%s/products/**",apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.PUT,String.format("%s/products/**",apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.DELETE,String.format("%s/products/**",apiPrefix)).hasRole(Role.ADMIN)
                            //order request
                            .requestMatchers(HttpMethod.POST,String.format("%s/order/**",apiPrefix)).hasRole(Role.USER)
                            .requestMatchers(HttpMethod.GET,String.format("%s/order/**",apiPrefix)).hasAnyRole(Role.USER,Role.ADMIN)
                            .requestMatchers(HttpMethod.PUT,String.format("%s/orders/**",apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.DELETE,String.format("%s/orders/**",apiPrefix)).hasRole(Role.ADMIN)
                            //order detail request
                            .requestMatchers(HttpMethod.POST,String.format("%s/order_details/**",apiPrefix)).hasRole(Role.USER)
                            .requestMatchers(HttpMethod.GET,String.format("%s/order_details/**",apiPrefix)).hasAnyRole(Role.USER,Role.ADMIN)
                            .requestMatchers(HttpMethod.PUT,String.format("%s/order_details/**",apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.DELETE,String.format("%s/order_details/**",apiPrefix)).hasRole(Role.ADMIN)
                            .anyRequest().authenticated();
                });
        return http.build();
    }

}

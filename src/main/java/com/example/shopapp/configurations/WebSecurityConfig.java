package com.example.shopapp.configurations;

import com.example.shopapp.filters.JwtTokenFilter;
import com.example.shopapp.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@Configuration
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebMvc
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;
    @Value("${api.prefix}")
    private String apiPrefix;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        http
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(request->
                {
                    request.requestMatchers(
                            String.format("%s/users/register",apiPrefix),
                            String.format("%s/users/login",apiPrefix)
                            )
                            .permitAll()
                            //category request
                            .requestMatchers(HttpMethod.GET,String.format("%s/categories**",apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.POST,String.format("%s/categories/**",apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.PUT,String.format("%s/categories/**",apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.DELETE,String.format("%s/categories/**",apiPrefix)).hasRole(Role.ADMIN)
                            //role
                            .requestMatchers(HttpMethod.GET,String.format("%s/roles",apiPrefix)).permitAll()
                            //product request
                            .requestMatchers(HttpMethod.GET,String.format("%s/products**",apiPrefix)).permitAll()
                            .requestMatchers(HttpMethod.POST,String.format("%s/products/**",apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.PUT,String.format("%s/products/**",apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.DELETE,String.format("%s/products/**",apiPrefix)).hasRole(Role.ADMIN)
                            //product image
                            .requestMatchers(HttpMethod.GET,String.format("%s/products/images/*",apiPrefix)).permitAll()
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
                })
                .csrf(AbstractHttpConfigurer::disable);
        http.securityMatcher(String.valueOf(EndpointRequest.toAnyEndpoint()));

        return http.build();
    }

}

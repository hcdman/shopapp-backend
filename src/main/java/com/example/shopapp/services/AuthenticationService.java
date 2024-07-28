package com.example.shopapp.services;

import com.example.shopapp.dto.ExchangeTokenRequest;
import com.example.shopapp.dto.UserDTO;
import com.example.shopapp.model.User;
import com.example.shopapp.repositories.httpclient.OutboundIdentityClient;
import com.example.shopapp.repositories.httpclient.OutboundUserClient;
import com.example.shopapp.responses.LoginResponse;
import com.example.shopapp.responses.OutboundUserResponse;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private  final OutboundIdentityClient outboundIdentityClient;
    private final OutboundUserClient outboundUserClient;
    private final UserService userService;
    @NonFinal
    @Value("${outbound.identity.client-id}")
    protected  String CLIENT_ID = "503745700164-1f1jcb22h6a1sv63taukkq7jn2lc1dot.apps.googleusercontent.com";
    @NonFinal
    @Value("${outbound.identity.client-secret}")
    protected String CLIENT_SECRET;
    @NonFinal
    @Value("${outbound.identity.redirect-uri}")
    protected String REDIRECT_URI = "http://localhost:4200/authenticate";
    @NonFinal
    protected String GRANT_TYPE = "authorization_code";
    public LoginResponse outboundAuthentication(String code) throws Exception {
        var response = outboundIdentityClient.exchangeToken(ExchangeTokenRequest.builder()
                        .code(code)
                        .clientId(CLIENT_ID)
                        .clientSecret(CLIENT_SECRET)
                        .redirectUri(REDIRECT_URI)
                        .grantType(GRANT_TYPE)
                .build());
        OutboundUserResponse userResponse = outboundUserClient.getUserInfo("json",response.getAccessToken());
        Optional<User> user = userService.getUserByUserIdentifier(userResponse.getEmail());
        if(!user.isPresent())
        {
            userService.createUser(UserDTO.builder()
                            .userIdentifier(userResponse.getEmail())
                            .fullName(userResponse.getName())
                            .password("123456")
                            .googleAccountId(1)
                            .roleId(1L)
                    .build());
        }
        String token = userService.login(userResponse.getEmail(),"123456", 1L);
        return LoginResponse.builder()
                .token(token)
                .message("Login with google successfully!")
                .build();
    }
}

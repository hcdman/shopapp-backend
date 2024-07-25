package com.example.shopapp.controller;
import com.example.shopapp.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/auth")
@RequiredArgsConstructor
public class    AuthenticationController {
    private  final AuthenticationService authenticationService;
    @PostMapping("/outbound/authentication")
    ResponseEntity<?> outboundAuthenticate(@RequestParam("code") String code) throws Exception {
        var result = authenticationService.outboundAuthentication(code);
        return ResponseEntity.ok().body(result);
    }
}

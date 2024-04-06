package com.example.shopapp.controller;

import com.example.shopapp.dto.UserDTO;
import com.example.shopapp.dto.UserLoginDTO;
import com.example.shopapp.responses.LoginResponse;
import com.example.shopapp.services.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.descriptor.LocalResolver;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;
    private final MessageSource messageSource;
    private final LocaleResolver localResolver;
    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO, BindingResult result)
    {
        try
        {
            if(result.hasErrors())
            {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            if(!userDTO.getPassword().equals(userDTO.getRetypePassword()))
            {
                return ResponseEntity.badRequest().body("Your retype password is not match");
            }
            userService.createUser(userDTO);
            return ResponseEntity.ok(userDTO);
        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDTO userLoginDTO , HttpServletRequest request)
    {
        //check login and create token
        //response token
        try {
            String token = userService.login(userLoginDTO.getPhoneNumber(),userLoginDTO.getPassword());
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setToken(token);
            Locale locale = localResolver.resolveLocale(request);
            loginResponse.setMessage(messageSource.getMessage("user.login.login_successfully",null,locale));
            return ResponseEntity.ok().body(loginResponse);
        } catch (Exception e) {
           return ResponseEntity.badRequest().body(LoginResponse.builder()
                   .message(e.getMessage()).build());
        }
    }
}

package com.example.shopapp.controller;

import com.example.shopapp.component.LocalizationUtil;
import com.example.shopapp.dto.UpdateUserDTO;
import com.example.shopapp.dto.UserDTO;
import com.example.shopapp.dto.UserLoginDTO;
import com.example.shopapp.model.User;
import com.example.shopapp.responses.LoginResponse;
import com.example.shopapp.responses.UserResponse;
import com.example.shopapp.services.UserService;
import com.example.shopapp.utils.MessageKeys;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final LocalizationUtil localizationUtil;


    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/foo")
    void handleFoo(HttpServletResponse response) throws IOException {
        response.sendRedirect("http://localhost:8088/oauth2/authorization/google");
    }
//    @GetMapping("/loginSuccess")
//    public ResponseEntity<?> loginSuccess(Authentication authentication) {
//
//        try {
//            if (authentication instanceof OAuth2AuthenticationToken) {
//                OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
//                OAuth2User oAuth2User = oAuth2AuthenticationToken.getPrincipal();
//                Map<String, Object> attributes = oAuth2User.getAttributes();
//                String email = (String) attributes.get("email");
//                String phoneNumber ="", passWord = "google";
//                //check exist user with email
//                Optional<User> user = userService.getUserByEmail(email);
//                if(user.isEmpty())
//                {
//                    String address = (String) attributes.get("address");
//                    String name = (String) attributes.get("name");
//                    UUID uuid = UUID.randomUUID();
//                    String randomString = uuid.toString().substring(0, 8);
//                    UserDTO userDTO = UserDTO.builder().fullName(name)
//                            .email(email)
//                            .address(address)
//                            .googleAccountId(1)
//                            .phoneNumber(randomString)
//                            .password("google")
//                            .roleId(1L)
//                            .build();
//                    userService.createUser(userDTO);
//                    phoneNumber = userDTO.getPhoneNumber();
//                }
//                else
//                {
//                    phoneNumber = user.get().getPhoneNumber();
//                }
//                String token = userService.loginSocialAccount(email,phoneNumber,passWord);
//                LoginResponse loginResponse = new LoginResponse();
//                loginResponse.setToken(token);
//                String message = localizationUtil.getLocalizedMessage(MessageKeys.LOGIN_SUCCESS);
//                loginResponse.setMessage(message);
//                return ResponseEntity.ok().body(loginResponse);
//            } else {
//                return ResponseEntity.badRequest().body(LoginResponse.builder()
//                        .message(localizationUtil.getLocalizedMessage(MessageKeys.LOGIN_FAILED)).build());
//            }
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
    @GetMapping("/loginFailed")
    public ResponseEntity<?> loginFailed() {
        return ResponseEntity.badRequest().body(LoginResponse.builder()
                .message(localizationUtil.getLocalizedMessage(MessageKeys.LOGIN_FAILED)).build());
    }
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
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDTO userLoginDTO)
    {
        //check login and create token
        //response token
        try {
            String token = userService.login(userLoginDTO.getPhoneNumber(),userLoginDTO.getPassword(), userLoginDTO.getRoleId());
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setToken(token);
            String message = localizationUtil.getLocalizedMessage(MessageKeys.LOGIN_SUCCESS);
            loginResponse.setMessage(message);
            return ResponseEntity.ok().body(loginResponse);
        } catch (Exception e) {
           return ResponseEntity.badRequest().body(LoginResponse.builder()
                   .message(localizationUtil.getLocalizedMessage(MessageKeys.LOGIN_FAILED,e.getMessage())).build());
        }
    }

    @PostMapping("/details")
    public ResponseEntity<?> getUserDetails(@RequestHeader("Authorization") String token)
    {
        try {
            String extractedToken = token.substring(7); //"bearer "
            User user = userService.getUserDetailFromToken(extractedToken);
            return  ResponseEntity.ok().body(UserResponse.fromUser(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/details/{userId}")
    public ResponseEntity<UserResponse> updateUserDetails(
            @PathVariable Long userId,
            @RequestBody UpdateUserDTO updatedUserDTO,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        try {

            String extractedToken = authorizationHeader.substring(7);
            User user = userService.getUserDetailFromToken(extractedToken);
            // Ensure that the user making the request matches the user being updated
            if (user.getId() != userId) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            User updatedUser = userService.updateUser(userId, updatedUserDTO);
            return ResponseEntity.ok(UserResponse.fromUser(updatedUser));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

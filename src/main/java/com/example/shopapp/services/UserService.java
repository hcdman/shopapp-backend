package com.example.shopapp.services;

import com.example.shopapp.component.JwtTokenUtil;
import com.example.shopapp.dto.UserDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.exceptions.PermissionDenyException;
import com.example.shopapp.model.Role;
import com.example.shopapp.model.User;
import com.example.shopapp.repositories.RoleRepository;
import com.example.shopapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    @Override
    public User createUser(UserDTO userDTO) throws Exception {
        String phoneNumber = userDTO.getPhoneNumber();
        if(userRepository.existsByPhoneNumber(phoneNumber))
        {
            throw new DataIntegrityViolationException("Phone number already exists!");
        }
        User newUser = User.builder()
                .fullName(userDTO.getFullName())
                .phoneNumber(userDTO.getPhoneNumber())
                .password(userDTO.getPassword())
                .address(userDTO.getAddress())
                .dateOfBirth(userDTO.getDateOfBirth())
                .facebookAccountId(userDTO.getFacebookAccountId())
                .googleAccountId(userDTO.getGoogleAccountId())
                .build();
        Role role = roleRepository.findById(userDTO.getRoleId()).orElseThrow(()->new DataNotFoundException("role not found"));
        //Don't allow register account with role admin
        if(role.getName().toUpperCase().equals("ADMIN"))
        {
            throw new PermissionDenyException("You can't not register account with role admin");
        }
        newUser.setRole(role);
        if(userDTO.getGoogleAccountId()==0&&userDTO.getFacebookAccountId()==0)
        {
            String password = userDTO.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            newUser.setPassword(encodedPassword);
        }
        return userRepository.save(newUser);
    }

    @Override
    public String login(String phoneNumber, String password,Long roleId) throws Exception {
        Optional<User> optionalUser = userRepository.findByPhoneNumber(phoneNumber);
        if(optionalUser.isEmpty())
        {
            throw new DataNotFoundException("Invalid phone number / password");
        }
        User existedUser = optionalUser.get();
        //check password
        if(existedUser.getGoogleAccountId()==0&&existedUser.getFacebookAccountId()==0)
        {
            if(!passwordEncoder.matches(password,existedUser.getPassword()))
            {
                throw new BadCredentialsException("Wrong phone number or password");
            }
        }
        Optional<Role> optionalRole = roleRepository.findById(roleId);
        if(optionalRole.isEmpty() || !roleId.equals(existedUser.getRole().getId())) {
            throw new DataNotFoundException("Consider your role !");
        }
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(phoneNumber,password);
        //authenticate with Java Spring Security
        authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        return jwtTokenUtil.genereateToken(existedUser); //return token
    }
}

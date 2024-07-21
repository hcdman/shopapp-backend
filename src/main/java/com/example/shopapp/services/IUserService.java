package com.example.shopapp.services;

import com.example.shopapp.dto.UpdateUserDTO;
import com.example.shopapp.dto.UserDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.model.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface IUserService {
    public User createUser(UserDTO userDTO) throws Exception;
    public String login(String phoneNumber, String password,Long roleId) throws Exception;
    public Optional<User> getUserByEmail(String email) throws Exception;


    public User getUserDetailFromToken(String token) throws Exception;
    User updateUser(Long userId, UpdateUserDTO updatedUserDTO) throws Exception;
}

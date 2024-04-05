package com.example.shopapp.services;

import com.example.shopapp.dto.UserDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.model.User;

public interface IUserService {
    public User createUser(UserDTO userDTO) throws DataNotFoundException;
    public String login(String phoneNumber, String password) throws Exception;
}

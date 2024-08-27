package com.application.sushi.service;

import org.springframework.stereotype.Service;

import com.application.sushi.domain.User;
import com.application.sushi.domain.request.ReqSignupDto;
import com.application.sushi.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User createNewUser(User user){
        return this.userRepository.save(user);
    }

    public User fetchUserByEmail(String email){
        return this.userRepository.findByEmail(email);
    }
    public User convertDtoToUser(ReqSignupDto reqLoginDto){
        User new_user = new User();
        new_user.setEmail(reqLoginDto.getUsername());
        new_user.setPassword(reqLoginDto.getUserpassword());
        new_user.setName(reqLoginDto.getName());
        new_user.setRole("USER");
        return new_user;
    }
    public boolean isEmailExist(String email){
        return this. userRepository.existsByEmail(email);
    }
}

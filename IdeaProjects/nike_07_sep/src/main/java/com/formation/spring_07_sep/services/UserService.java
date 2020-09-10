package com.formation.spring_07_sep.services;

import com.formation.spring_07_sep.models.User;
import com.formation.spring_07_sep.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
       return userRepository.findById(email);
    }
}

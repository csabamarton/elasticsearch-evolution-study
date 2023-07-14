package com.example.elastic.poc.service;

import com.example.elastic.poc.dao.UserRepository;
import com.example.elastic.poc.model.User;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        Iterable<User> iterable = userRepository.findAll();
       return IterableUtils.toList(iterable);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }
}

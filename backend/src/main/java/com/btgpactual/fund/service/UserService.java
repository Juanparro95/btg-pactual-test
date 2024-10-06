package com.btgpactual.fund.service;

import com.btgpactual.fund.model.User;
import com.btgpactual.fund.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 *
 * @author Davidparro
 */

@Service
public class UserService {
    @Autowired
    private IUserRepository userRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findUserById(String userId) {
        return userRepository.findById(userId);
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }
}

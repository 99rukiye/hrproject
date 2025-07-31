package com.hr.hrproject.service;

import com.hr.hrproject.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> getByEmail(String email);
    List<User> getAllUsers();
    Optional<User> getById(Long id);
    void save(User user);
    void delete(Long id);

}
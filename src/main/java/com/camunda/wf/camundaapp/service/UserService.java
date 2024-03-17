package com.camunda.wf.camundaapp.service;

import com.camunda.wf.camundaapp.model.User;
import com.camunda.wf.camundaapp.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;

    public User login(User user){
        Optional<User> opt = userRepo.findByUsernameAndPassword(user.getUsername(), user.getPassword());
        return opt.orElse(null);
    }
}

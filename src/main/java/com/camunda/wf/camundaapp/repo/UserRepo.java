package com.camunda.wf.camundaapp.repo;

import com.camunda.wf.camundaapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByUsernameAndPassword(String username, String password);
}

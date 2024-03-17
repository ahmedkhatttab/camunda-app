package com.camunda.wf.camundaapp.controller;

import com.camunda.wf.camundaapp.model.User;
import com.camunda.wf.camundaapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final static String SUCCESS_MSG = "Success";

    private final UserService userService;
    private final RuntimeService runtimeService;

    @PostMapping("/login")
    ResponseEntity<String> login(@RequestBody @Valid User user){
        User principle = userService.login(user);
        if(principle!=null){
            ProcessInstance processDef = runtimeService.startProcessInstanceByKey("login_process",
                    principle.getId().toString());
        }
        return ResponseEntity.ok(SUCCESS_MSG);
    }
}

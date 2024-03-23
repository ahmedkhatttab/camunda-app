package com.camunda.wf.camundaapp.controller;

import com.camunda.wf.camundaapp.dto.camunda.PayloadDTO;
import com.camunda.wf.camundaapp.dto.camunda.VariableDTO;
import com.camunda.wf.camundaapp.model.Product;
import com.camunda.wf.camundaapp.model.User;
import com.camunda.wf.camundaapp.repo.ProductRepo;
import com.camunda.wf.camundaapp.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final static String SUCCESS_MSG = "Success";

    @Value("${camunda-base-url}")
    private String camundaBaseUrl;

    private final UserService userService;
    private final RuntimeService runtimeService;
    private final ProductRepo productRepo;

    private final RestTemplate restTemplate;

    @PostMapping("/login")
    ResponseEntity<String> login(@RequestBody @Valid User user){
        User principle = userService.login(user);
        if(principle!=null){
            ProcessInstance processDef = runtimeService.startProcessInstanceByKey("login_process",
                    principle.getId().toString());
        }
        return ResponseEntity.ok(SUCCESS_MSG);
    }


    @GetMapping("/products")
    public ResponseEntity<JsonNode> createProduct(){

        PayloadDTO payloadDTO = new PayloadDTO();
        payloadDTO.setBusinessKey(UUID.randomUUID().toString());

        // start process and get processInstanceId
        JsonNode res = restTemplate.postForObject(camundaBaseUrl+"/process-definition/key/{key}/start", payloadDTO,
                JsonNode.class,
                "login_process");

        String processInstanceId = res.get("id").asText();

        // get TaskID
        Map<String, String> queryParam = new HashMap<>();
        queryParam.put("processInstanceId", processInstanceId);
        res = restTemplate.getForObject(camundaBaseUrl+"/task?processInstanceId={processInstanceId}", JsonNode.class,
                processInstanceId);
//        res = restTemplate.getForObject(camundaBaseUrl+"/task?processInstanceId="+processInstanceId, JsonNode.class);
        String taskId = res.get(0).get("id").asText();

        // add variables to execution scope
        Map<String, VariableDTO> body = new HashMap<>();
        VariableDTO prod1 = new VariableDTO();
        prod1.setValue(new Product(1L, "CAR WASHER", 20.0));
        body.put("product", prod1);
        payloadDTO.setVariables(body);

        // add execution variables to response payload
        payloadDTO.setWithVariablesInReturn(true);

        // complete task
        res = restTemplate.postForObject(camundaBaseUrl + "/task/{id}/complete", payloadDTO, JsonNode.class, taskId);

        return ResponseEntity.ok(res);
    }
}

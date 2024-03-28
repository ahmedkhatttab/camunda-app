package com.camunda.wf.camundaapp.controller;

import com.camunda.wf.camundaapp.clients.CamundaClient;
import com.camunda.wf.camundaapp.dto.camunda.request.TaskRequestDto;
import com.camunda.wf.camundaapp.dto.camunda.request.VariableDTO;
import com.camunda.wf.camundaapp.dto.camunda.response.ProcessInstanceDto;
import com.camunda.wf.camundaapp.dto.camunda.response.TaskDto;
import com.camunda.wf.camundaapp.model.Product;
import com.camunda.wf.camundaapp.repo.ProductRepo;
import com.camunda.wf.camundaapp.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class ProcessController {

    private final static String SUCCESS_MSG = "Success";

    @Value("${camunda-base-url}")
    private String camundaBaseUrl;

    private final UserService userService;
    private final RuntimeService runtimeService;
    private final ProductRepo productRepo;

    private final RestTemplate restTemplate;
    private final CamundaClient camundaClient;


    @GetMapping("/requests")
    public ResponseEntity<String> createProduct(){

        // start process and get processInstanceId
        ProcessInstanceDto processInstance = camundaClient.startProcessByKey("login_process", null);
        String processInstanceId = processInstance.getId();

        // get TaskID
        TaskDto taskInstance = camundaClient.getTaskIdByProcessInstanceId(processInstanceId);
        String taskId = taskInstance.getId();

        // add variables to execution scope
        TaskRequestDto taskRequestDto = new TaskRequestDto();
        Map<String, VariableDTO> body = new HashMap<>();
        VariableDTO prod1 = new VariableDTO();
        prod1.setValue(new Product(1L, "CAR WASHER", 20.0));
        body.put("product", prod1);
        taskRequestDto.setVariables(body);

        // add execution variables to response payload
        taskRequestDto.setWithVariablesInReturn(true);

        // complete task
        camundaClient.completeTask(taskId, taskRequestDto);

        return ResponseEntity.ok("Success");
    }
}

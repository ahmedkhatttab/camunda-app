package com.camunda.wf.camundaapp.controller;

import com.camunda.wf.camundaapp.camunda.dto.request.ProcessInstanceRequestDto;
import com.camunda.wf.camundaapp.camunda.dto.response.ProcessInstanceDto;
import com.camunda.wf.camundaapp.camunda.dto.response.TaskDto;
import com.camunda.wf.camundaapp.clients.CamundaClient;
import com.camunda.wf.camundaapp.model.OrderRequest;
import com.camunda.wf.camundaapp.repo.OrderRequestRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
@Slf4j
public class ProcessRequestController {

    private final static String SUCCESS_MSG = "Success";

    private final CamundaClient camundaClient;

    private final OrderRequestRepo repo;


    @PostMapping
    @Transactional
    public ResponseEntity<String> createProduct(@RequestBody @Valid OrderRequest request){

        String processDefKey = "request_process";

        // start process and get processInstanceId
        ProcessInstanceRequestDto instanceRequestDto = new ProcessInstanceRequestDto();
        instanceRequestDto.setBusinessKey(UUID.randomUUID().toString());
        ProcessInstanceDto processInstance = camundaClient.startProcessByKey(processDefKey, instanceRequestDto);
        String processInstanceId = processInstance.getId();

        // save entity
        request.setProcessInstanceId(processInstanceId);
        repo.save(request);

        // get TaskID
        TaskDto taskDto = camundaClient.getTaskIdByProcessInstanceId(processInstanceId);
        String taskId = taskDto.getId();

        // complete task
        camundaClient.completeTask(taskId, null);

        log.info("-------------------- ?????????????????? --------------------");

        return ResponseEntity.ok("Success");
    }

}

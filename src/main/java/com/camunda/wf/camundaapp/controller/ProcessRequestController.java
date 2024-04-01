package com.camunda.wf.camundaapp.controller;

import com.camunda.wf.camundaapp.camunda.dto.request.ProcessInstanceRequestDto;
import com.camunda.wf.camundaapp.camunda.dto.response.ProcessInstanceDto;
import com.camunda.wf.camundaapp.camunda.dto.response.TaskDto;
import com.camunda.wf.camundaapp.clients.CamundaClient;
import com.camunda.wf.camundaapp.model.OrderRequest;
import com.camunda.wf.camundaapp.model.constant.ERequestStatus;
import com.camunda.wf.camundaapp.repo.OrderRequestRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
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
        request.setBusinessKey(instanceRequestDto.getBusinessKey());
        repo.save(request);

        // get TaskID
        TaskDto taskDto = camundaClient.getTaskByProcessInstanceId(processInstanceId);
        String taskId = taskDto.getId();

        // complete task
        camundaClient.completeTask(taskId, null);

        return ResponseEntity.ok("Success");
    }


    @PutMapping("/admin/review/{processInstanceId}")
    @Transactional
    public ResponseEntity<String> createProduct(@PathVariable String processInstanceId){

        Optional<OrderRequest> orderRequest = repo.findByProcessInstanceId(processInstanceId);
        if(orderRequest.isEmpty()){
            throw new RuntimeException("Request Not Found");
        }
        else if(orderRequest.get().getStatus()!= ERequestStatus.REQUEST_SENT){
            throw new RuntimeException("Invalid Request Status");
        }

        // get TaskID
        TaskDto taskDto = camundaClient.getTaskByBusinessKey(processInstanceId);
        String taskId = taskDto.getId();

        // complete task
        camundaClient.completeTask(taskId, null);

        return ResponseEntity.ok("Success");
    }

}

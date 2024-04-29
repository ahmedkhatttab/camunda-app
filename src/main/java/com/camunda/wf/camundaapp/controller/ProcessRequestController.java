package com.camunda.wf.camundaapp.controller;

import com.camunda.wf.camundaapp.camunda.dto.request.ProcessInstanceRequestDto;
import com.camunda.wf.camundaapp.camunda.dto.request.TaskRequestDto;
import com.camunda.wf.camundaapp.camunda.dto.request.VariableDTO;
import com.camunda.wf.camundaapp.camunda.dto.response.ProcessInstanceDto;
import com.camunda.wf.camundaapp.camunda.dto.response.TaskDto;
import com.camunda.wf.camundaapp.clients.CamundaClient;
import com.camunda.wf.camundaapp.model.OrderRequest;
import com.camunda.wf.camundaapp.model.constant.AdminDecision;
import com.camunda.wf.camundaapp.model.constant.ERequestStatus;
import com.camunda.wf.camundaapp.repo.OrderRequestRepo;
import com.camunda.wf.camundaapp.service.utils.CamundaUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> createRequest(@RequestBody @Valid OrderRequest request){

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
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<String> reviewRequest(@PathVariable String processInstanceId){

        Optional<OrderRequest> orderRequest = repo.findByProcessInstanceId(processInstanceId);
        if(orderRequest.isEmpty()){
            throw new RuntimeException("Request Not Found");
        }
        else if(orderRequest.get().getStatus()!= ERequestStatus.REQUEST_SENT){
            throw new RuntimeException("Invalid Request Status");
        }

        // get TaskID
        TaskDto taskDto = camundaClient.getTaskByBusinessKey(orderRequest.get().getBusinessKey());
        String taskId = taskDto.getId();

        // complete task
        camundaClient.completeTask(taskId, null);

        return ResponseEntity.ok("Success");
    }


    @PutMapping("/admin/decision/{processInstanceId}")
    @Transactional
    public ResponseEntity<String> takeDecision(@PathVariable String processInstanceId,
                                               @RequestParam AdminDecision decision){

        Optional<OrderRequest> orderRequest = repo.findByProcessInstanceId(processInstanceId);
        if(orderRequest.isEmpty()){
            throw new RuntimeException("Request Not Found");
        }
        else if(orderRequest.get().getStatus()!= ERequestStatus.REQUEST_UNDER_REVIEW){
            throw new RuntimeException("Invalid Request Status");
        }

        // get TaskID
        TaskDto taskDto = camundaClient.getTaskByBusinessKey(orderRequest.get().getBusinessKey());
        String taskId = taskDto.getId();

        Map<String, VariableDTO> execVars = null;
        execVars = CamundaUtils.addToExecutionScope("adminDecision", decision);

        TaskRequestDto taskRequestDto = new TaskRequestDto();
        taskRequestDto.setVariables(execVars);

        // complete task
        camundaClient.completeTask(taskId, taskRequestDto);

        return ResponseEntity.ok("Success");
    }

}

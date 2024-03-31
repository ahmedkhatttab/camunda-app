package com.camunda.wf.camundaapp.controller;

import com.camunda.wf.camundaapp.camunda.dto.request.ProcessInstanceRequestDto;
import com.camunda.wf.camundaapp.camunda.dto.response.ProcessInstanceDto;
import com.camunda.wf.camundaapp.camunda.dto.response.TaskDto;
import com.camunda.wf.camundaapp.clients.CamundaClient;
import com.camunda.wf.camundaapp.model.OrderRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
@Slf4j
public class ProcessRequestController {

    private final static String SUCCESS_MSG = "Success";

    private final CamundaClient camundaClient;


    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody @Valid OrderRequest request){
        String processDefKey = "request_process";

        // start process and get processInstanceId
        ProcessInstanceRequestDto instanceRequestDto = new ProcessInstanceRequestDto();
        instanceRequestDto.setBusinessKey(UUID.randomUUID().toString());
        ProcessInstanceDto processInstance = camundaClient.startProcessByKey(processDefKey, instanceRequestDto);
        String processInstanceId = processInstance.getId();

        // get TaskID
        TaskDto taskDto = camundaClient.getTaskIdByProcessInstanceId(processInstanceId);
        String taskId = taskDto.getId();

        // complete task
        camundaClient.completeTask(taskId, null);

        return ResponseEntity.ok("Success");
    }

}

package com.camunda.wf.camundaapp.clients;


import com.camunda.wf.camundaapp.dto.camunda.request.TaskRequestDto;
import com.camunda.wf.camundaapp.dto.camunda.response.ProcessInstanceDto;
import com.camunda.wf.camundaapp.dto.camunda.response.TaskDto;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CamundaClient {

    @Value("${camunda-base-url}")
    private String camundaBaseUrl;
    private final RestTemplate restTemplate;


    public ProcessInstanceDto startProcessByKey(String processDefKey, String businessKey){
        businessKey = (StringUtils.isBlank(businessKey) ? UUID.randomUUID().toString(): businessKey);
        return restTemplate.postForObject(
                camundaBaseUrl+"/process-definition/key/{key}/start",
                businessKey, ProcessInstanceDto.class,processDefKey);
    }

    public TaskDto getTaskIdByProcessInstanceId(String processInstanceId){
        ResponseEntity<List<TaskDto>> taskList =
                restTemplate.exchange(camundaBaseUrl + "/task?processInstanceId={processInstanceId}",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<TaskDto>>() {
                }, processInstanceId);

        return taskList.getBody().get(0);
    }

    public void completeTask(String taskId, TaskRequestDto payload){
        restTemplate.postForObject(camundaBaseUrl + "/task/{id}/complete", payload, JsonNode.class, taskId);
    }
}

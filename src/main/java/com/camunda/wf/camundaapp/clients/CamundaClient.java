package com.camunda.wf.camundaapp.clients;


import com.camunda.wf.camundaapp.camunda.dto.request.ProcessInstanceRequestDto;
import com.camunda.wf.camundaapp.camunda.dto.request.TaskRequestDto;
import com.camunda.wf.camundaapp.camunda.dto.response.ProcessInstanceDto;
import com.camunda.wf.camundaapp.camunda.dto.response.TaskDto;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
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


    public ProcessInstanceDto startProcessByKey(String processDefKey, ProcessInstanceRequestDto dto){

        HttpEntity<ProcessInstanceRequestDto> requestEntity = new HttpEntity<>(dto, getHttpHeaders());

        return restTemplate.exchange(
                camundaBaseUrl+"/process-definition/key/{key}/start",
                HttpMethod.POST, requestEntity, ProcessInstanceDto.class, processDefKey).getBody();
    }

    public TaskDto getTaskByProcessInstanceId(String processInstanceId){
        HttpEntity requestEntity = new HttpEntity(null, getHttpHeaders());

        ResponseEntity<List<TaskDto>> taskList =
                restTemplate.exchange(camundaBaseUrl + "/task?processInstanceId={processInstanceId}",
                HttpMethod.GET, requestEntity, new ParameterizedTypeReference<List<TaskDto>>() {
                }, processInstanceId);

        return taskList.getBody().get(0);
    }

    public TaskDto getTaskByBusinessKey(String businessKey){
        HttpEntity requestEntity = new HttpEntity(null, getHttpHeaders());
        ResponseEntity<List<TaskDto>> taskList =
                restTemplate.exchange(camundaBaseUrl + "/task?processInstanceBusinessKey={businessKey}",
                        HttpMethod.GET, requestEntity, new ParameterizedTypeReference<List<TaskDto>>() {
                        }, businessKey);

        return taskList.getBody().get(0);
    }

    public void completeTask(String taskId, TaskRequestDto payload){
        HttpEntity<TaskRequestDto> requestEntity = new HttpEntity<>(payload, getHttpHeaders());

        restTemplate.exchange(camundaBaseUrl + "/task/{id}/complete",
                HttpMethod.POST, requestEntity, Void.class, taskId);
    }


    private HttpHeaders getHttpHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Object auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth!=null){
            if(auth instanceof JwtAuthenticationToken){
                headers.add(HttpHeaders.AUTHORIZATION,
                        "Bearer "+((JwtAuthenticationToken)auth).getToken().getTokenValue());
            }
        }

        return headers;
    }
}

package com.camunda.wf.camundaapp.clients;


import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CamundaClient {

    @Value("${camunda-base-url}")
    private String camundaBaseUrl;
    private final RestTemplate restTemplate;


    public JsonNode startProcessByKey(String processDefKey, String businessKey){
        businessKey = (StringUtils.isBlank(businessKey) ? UUID.randomUUID().toString(): businessKey);
        return restTemplate.postForObject(
                camundaBaseUrl+"/process-definition/key/{key}/start",
                businessKey, JsonNode.class,processDefKey);
    }

    public JsonNode getTaskIdByProcessInstanceId(String processInstanceId){
//        Map<String, String> queryParam = new HashMap<>();
//        queryParam.put("processInstanceId", processInstanceId);
        return restTemplate.getForObject(camundaBaseUrl + "/task?processInstanceId={processInstanceId}",
                JsonNode.class, processInstanceId);
    }
}

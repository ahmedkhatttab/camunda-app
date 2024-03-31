package com.camunda.wf.camundaapp.camunda.dto.response;

import lombok.Data;

@Data
public class ProcessInstanceDto {
    private String id;
    private String definitionId;
    private String businessKey;
    private String caseInstanceId;
    private boolean ended;
    private boolean suspended;
}

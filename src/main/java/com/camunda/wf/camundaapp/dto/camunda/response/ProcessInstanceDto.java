package com.camunda.wf.camundaapp.dto.camunda.response;

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

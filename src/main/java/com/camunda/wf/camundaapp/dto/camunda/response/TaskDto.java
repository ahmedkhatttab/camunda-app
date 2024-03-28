package com.camunda.wf.camundaapp.dto.camunda.response;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class TaskDto {
        private String id;
        private String name;
        private String assignee;
        private OffsetDateTime created;
        private OffsetDateTime due;
        private OffsetDateTime followUp;
        private OffsetDateTime lastUpdated;
        private String delegationState;
        private String description;
        private String executionId;
        private String owner;
        private String parentTaskId;
        private int priority;
        private String processDefinitionId;
        private String processInstanceId;
        private String caseDefinitionId;
        private String caseInstanceId;
        private String caseExecutionId;
        private String taskDefinitionKey;
        private boolean suspended;
        private String formKey;
        private CamundaFormRef camundaFormRef;
        private String tenantId;
}


@Data
class CamundaFormRef {
    private String key;
    private String binding;
    private int version;
}

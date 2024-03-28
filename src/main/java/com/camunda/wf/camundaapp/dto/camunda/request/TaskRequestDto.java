package com.camunda.wf.camundaapp.dto.camunda.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TaskRequestDto extends AbstractCamundaRequestWrapper{

    @JsonProperty("withVariablesInReturn")
    private Boolean withVariablesInReturn;
}

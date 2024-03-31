package com.camunda.wf.camundaapp.camunda.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProcessInstanceRequestDto extends AbstractCamundaRequestWrapper{
    @JsonProperty("businessKey")
    private String businessKey;
}

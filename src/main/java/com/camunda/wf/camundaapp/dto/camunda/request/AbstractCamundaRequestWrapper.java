package com.camunda.wf.camundaapp.dto.camunda.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;


@Data
public abstract class AbstractCamundaRequestWrapper {

    @JsonProperty("variables")
    private Map<String, VariableDTO> variables;

    // with process
//    @JsonProperty("businessKey")
//    private String businessKey;

}

package com.camunda.wf.camundaapp.dto.camunda;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;


@Getter
@Setter
public class PayloadDTO {

    @JsonProperty("variables")
    private Map<String, VariableDTO> variables;

    // with process
    @JsonProperty("businessKey")
    private String businessKey;

    // with task
    @JsonProperty("withVariablesInReturn")
    private Boolean withVariablesInReturn;

}

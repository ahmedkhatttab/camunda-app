package com.camunda.wf.camundaapp.camunda.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;


@Data
public abstract class AbstractCamundaRequestWrapper {

    @JsonProperty("variables")
    private Map<String, VariableDTO> variables;
}

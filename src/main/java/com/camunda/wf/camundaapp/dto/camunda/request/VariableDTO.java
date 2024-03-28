package com.camunda.wf.camundaapp.dto.camunda.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VariableDTO {

    @JsonProperty("value")
    private Object value;

    @JsonProperty("type")
    private String type;
}

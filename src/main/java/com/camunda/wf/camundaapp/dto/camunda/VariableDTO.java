package com.camunda.wf.camundaapp.dto.camunda;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VariableDTO {

    @JsonProperty("value")
    private Object value;

    @JsonProperty("type")
    private String type;

    // Getters and setters

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

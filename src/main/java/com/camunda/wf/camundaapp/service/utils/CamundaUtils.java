package com.camunda.wf.camundaapp.service.utils;

import com.camunda.wf.camundaapp.camunda.dto.request.VariableDTO;

import java.util.HashMap;
import java.util.Map;

public class CamundaUtils {

    public static Map<String, VariableDTO> addToExecutionScope(String key, Object value){
        Map<String, VariableDTO> variables = new HashMap<>();
        VariableDTO var = new VariableDTO();
        var.setValue(value);
        variables.put(key, var);
        return variables;
    }

    public static Map<String, VariableDTO> appendToExecutionScope(Map<String, VariableDTO> variables,
                                                               String key, Object value){
        VariableDTO var = new VariableDTO();
        var.setValue(value);
        variables.put(key, var);
        return variables;
    }
}

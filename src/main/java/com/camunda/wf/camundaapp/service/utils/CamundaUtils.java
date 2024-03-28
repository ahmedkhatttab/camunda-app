package com.camunda.wf.camundaapp.service.utils;

import com.camunda.wf.camundaapp.dto.camunda.request.VariableDTO;
import com.camunda.wf.camundaapp.model.Product;
import org.springframework.stereotype.Component;

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
}

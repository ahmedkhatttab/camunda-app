package com.camunda.wf.camundaapp.camunda.serviceTask;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.runtime.MessageCorrelationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageSendService implements JavaDelegate {

    @Autowired
    private RuntimeService runtimeService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String messageName = (String) execution.getVariableLocal("messageName");
        MessageCorrelationBuilder mcb = runtimeService.createMessageCorrelation(messageName);
        execution.getVariablesLocal().entrySet().stream().forEach((k)->{
            if(!k.getKey().equalsIgnoreCase(messageName)){
                mcb.setVariable(k.getKey(), k.getValue());
            }
        });
        mcb.correlate();
    }
}

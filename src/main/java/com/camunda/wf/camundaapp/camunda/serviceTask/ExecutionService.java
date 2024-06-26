package com.camunda.wf.camundaapp.camunda.serviceTask;

import com.camunda.wf.camundaapp.model.OrderRequest;
import com.camunda.wf.camundaapp.repo.OrderRequestRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class ExecutionService implements JavaDelegate {

    private final OrderRequestRepo orderRepo;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("-------------------- ADD TO EXECUTION --------------------");
        String processInstanceId = execution.getProcessInstanceId();
        OrderRequest orderRequest = orderRepo.findByProcessInstanceId(processInstanceId).get();
        log.info("{}", orderRequest);
        execution.setVariable("order", orderRequest);
    }
}

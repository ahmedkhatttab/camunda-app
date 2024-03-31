package com.camunda.wf.camundaapp.camunda.serviceTask;

import com.camunda.wf.camundaapp.model.OrderRequest;
import com.camunda.wf.camundaapp.model.constant.ERequestStatus;
import com.camunda.wf.camundaapp.repo.OrderRequestRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

@RequiredArgsConstructor
@Slf4j
public class RequestStatusService implements JavaDelegate {

    private final OrderRequestRepo orderRepo;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.debug("-------------------- CHANGE REQUEST STATUS --------------------");
        OrderRequest orderRequest = (OrderRequest) execution.getVariable("order");
        String requiredStatus = (String) execution.getVariable("req_status");
        orderRequest.setStatus(ERequestStatus.valueOf(requiredStatus));
        orderRepo.save(orderRequest);
    }
}

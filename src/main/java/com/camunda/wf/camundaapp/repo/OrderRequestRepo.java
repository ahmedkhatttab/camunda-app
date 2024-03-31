package com.camunda.wf.camundaapp.repo;

import com.camunda.wf.camundaapp.model.OrderRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRequestRepo extends JpaRepository<OrderRequest, Long> {
    Optional<OrderRequest> findByProcessInstanceId(String processInstanceId);
}

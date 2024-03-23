package com.camunda.wf.camundaapp.repo;

import com.camunda.wf.camundaapp.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<Product, Long> {
}

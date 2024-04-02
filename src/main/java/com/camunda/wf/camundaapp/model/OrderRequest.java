package com.camunda.wf.camundaapp.model;

import com.camunda.wf.camundaapp.model.constant.ERequestStatus;
import com.camunda.wf.camundaapp.model.constant.ERequestType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "order_request")
@Getter
@Setter
@NoArgsConstructor
public class OrderRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 1, max = 255)
    @Column(name = "item", nullable = false)
    private String item;

    @Min(0)
    @Column(name = "price", nullable = false)
    private Long price;

    @NotNull
    @Column(name = "type", nullable = false)
    private ERequestType type;

    @Column(name = "process_instance_id", nullable = false)
    private String processInstanceId;

    @Column(name = "business_key")
    private String businessKey;

    @Column(name = "request_status")
    private ERequestStatus status;
}



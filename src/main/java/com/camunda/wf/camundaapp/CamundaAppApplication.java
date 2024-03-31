package com.camunda.wf.camundaapp;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@RequiredArgsConstructor
public class CamundaAppApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(CamundaAppApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
	}


	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}
}

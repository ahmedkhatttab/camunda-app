package com.camunda.wf.camundaapp;

import com.camunda.wf.camundaapp.model.User;
import com.camunda.wf.camundaapp.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@RequiredArgsConstructor
public class CamundaAppApplication implements CommandLineRunner {

	private final UserRepo userRepo;

	public static void main(String[] args) {
		SpringApplication.run(CamundaAppApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		User user1 = new User();
		user1.setUsername("ahmedkhatab");
		user1.setPassword("1234");
		userRepo.save(user1);
	}


	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}
}

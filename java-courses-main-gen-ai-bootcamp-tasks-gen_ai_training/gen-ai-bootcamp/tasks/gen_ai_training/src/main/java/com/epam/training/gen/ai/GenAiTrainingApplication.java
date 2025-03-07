package com.epam.training.gen.ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:/config/application.properties")
public class GenAiTrainingApplication {

	public static void main(String[] args) {
		System.out.println("just checking");
		SpringApplication.run(GenAiTrainingApplication.class, args);
	}

}

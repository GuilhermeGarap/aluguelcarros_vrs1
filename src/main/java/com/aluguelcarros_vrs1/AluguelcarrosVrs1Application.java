package com.aluguelcarros_vrs1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AluguelcarrosVrs1Application {

	public static void main(String[] args) {
		SpringApplication.run(AluguelcarrosVrs1Application.class, args);
	}

}

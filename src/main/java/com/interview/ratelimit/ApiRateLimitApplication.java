package com.interview.ratelimit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})

public class ApiRateLimitApplication {
	public static void main(String[] args) {
		SpringApplication.run(ApiRateLimitApplication.class, args);
	}
}
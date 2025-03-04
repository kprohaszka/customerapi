package com.example.customerapi;

import com.example.customerapi.util.KeyGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CustomerApiApplication {

	public static void main(String[] args) {
		String secretKey = KeyGenerator.generateSecretKey();
		System.setProperty("JWT_SECRET_KEY", secretKey);
		SpringApplication.run(CustomerApiApplication.class, args);
	}

}

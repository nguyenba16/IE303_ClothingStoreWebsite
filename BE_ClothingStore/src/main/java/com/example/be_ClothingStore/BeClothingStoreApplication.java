package com.example.be_ClothingStore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class BeClothingStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(BeClothingStoreApplication.class, args);
	}
}

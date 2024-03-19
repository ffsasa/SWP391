package com.bookingBirthday.bookingbirthdayforkids;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@OpenAPIDefinition
@EnableAsync
public class BookingBirthdayForKidsApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookingBirthdayForKidsApplication.class, args);
	}

}

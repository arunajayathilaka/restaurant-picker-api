package com.arunadj.restaurantpickerapi;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableEncryptableProperties
public class RestaurantPickerApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestaurantPickerApiApplication.class, args);
	}

}

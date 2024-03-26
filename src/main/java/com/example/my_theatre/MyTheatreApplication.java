package com.example.my_theatre;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.my_theatre.mapper")
public class MyTheatreApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyTheatreApplication.class, args);
	}

}

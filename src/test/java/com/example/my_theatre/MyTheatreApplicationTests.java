package com.example.my_theatre;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest

@MapperScan("com.wen.mybatis_plus.mapper")
class MyTheatreApplicationTests {

	@Test
	void contextLoads() {


	}

}

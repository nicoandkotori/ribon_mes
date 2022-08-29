package com;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@MapperScan("com.web.**.mapper")
@SpringBootApplication
public class MesWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(MesWebApplication.class, args);
	}





}

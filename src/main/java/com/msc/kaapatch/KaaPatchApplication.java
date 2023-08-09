package com.msc.kaapatch;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.msc.kaapatch.dao.mapper")
public class KaaPatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(KaaPatchApplication.class, args);
	}

}

package edu.rlperez.java_general_web_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableScheduling
public class JavaGeneralWebDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(JavaGeneralWebDemoApplication.class, args);
	}

}

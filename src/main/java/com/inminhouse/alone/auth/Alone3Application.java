package com.inminhouse.alone.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.inminhouse.alone.auth.config.AppProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class Alone3Application {

	public static void main(String[] args) {
		SpringApplication.run(Alone3Application.class, args);
	}
	
}

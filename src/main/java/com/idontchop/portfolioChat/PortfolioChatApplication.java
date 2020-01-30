package com.idontchop.portfolioChat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication (scanBasePackages = { "com.idontchop"})
@ComponentScan(basePackages = "com.idontchop")
@RestController
public class PortfolioChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(PortfolioChatApplication.class, args);
	}
	
	@RequestMapping ("/helloWorld")
	public String helloWorld() {
		return "Hello Future Chat App";
	}
	
	@RequestMapping ("/user")
	public Authentication getPrincipal (Authentication auth) {
		return auth;
	}

}

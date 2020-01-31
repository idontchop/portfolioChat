package com.idontchop.portfolioChat;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idontchop.portfolioChat.model.MessageThread;
import com.idontchop.portfolioChat.model.User;
import com.idontchop.portfolioChat.repositories.MessageRepository;
import com.idontchop.portfolioChat.repositories.MessageThreadRepository;
import com.idontchop.portfolioChat.repositories.UserRepository;

@SpringBootApplication (scanBasePackages = { "com.idontchop"})
@ComponentScan(basePackages = "com.idontchop")
@RestController
public class PortfolioChatApplication implements CommandLineRunner {
	
	@Autowired
	private MessageThreadRepository mtRepo;
	
	@Autowired
	private UserRepository uRepo;

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

	
	@Override
	public void run(String... args) throws Exception {
		
		/*
		User u1 = new User("17");
		User u2 = new User("3");
		
		uRepo.save(u1);
		uRepo.save(u2);
		
		MessageThread m = new MessageThread();
		m.setMembers(new ArrayList<User>());
		m.addMember(u1);
		m.addMember(u2);
		
		mtRepo.save(m);
		*/	
	}


}

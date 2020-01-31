package com.idontchop.portfolioChat;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.idontchop.portfolioChat.model.Message;
import com.idontchop.portfolioChat.repositories.MessageRepository;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
class PortfolioChatApplicationTests {

	@Autowired
	private MessageRepository mRepo;
	
	@Test
	void contextLoads() {
	}
	
	@Test
	@WithMockUser(value = "17")
	void findTests () {
		
		Iterable<Message> l = mRepo.findAllByMessageThread_id(1);
		
		assertTrue ( l.iterator().hasNext() );
		
		l.forEach( t -> System.out.println(t.getId()));
	}

}

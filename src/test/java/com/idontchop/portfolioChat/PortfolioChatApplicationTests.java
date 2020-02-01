package com.idontchop.portfolioChat;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.idontchop.portfolioChat.model.Message;
import com.idontchop.portfolioChat.model.User;
import com.idontchop.portfolioChat.repositories.MessageRepository;
import com.idontchop.portfolioChat.repositories.UserRepository;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
class PortfolioChatApplicationTests {

	@Autowired
	private MessageRepository mRepo;
	
	@Autowired
	private UserRepository uRepo;
	
	@Test
	void contextLoads() {
	}
	
	
	
	@Test
	@WithMockUser(value = "17")
	void findTests () {
		
		Iterable<Message> l = mRepo.findAllByMessageThread_id(1);
		
		assertTrue ( l.iterator().hasNext() );
		
		l.forEach( t -> System.out.println(t.getId()));
		
		List<Long> lList = new ArrayList<>();
		
		lList.add(1L); lList.add(2L);
		
		assertTrue ( mRepo.findAllById( lList ).iterator().hasNext() );
		
	}


	@Test
	@WithMockUser ( value = "17" )
	void messageRepoTests () {
		
		Optional<Message> m = mRepo.findById(1L);
		
		assertTrue ( m.isPresent() );
		
		m = mRepo.findById(2L);
		
		assertTrue ( m.isEmpty() );
		
	}
	
	
	@WithMockUser ( value = "17" )
	void messageRepoDeleteTests () {
		
		Optional<Message> m = mRepo.findById(1L);
		if (m.isPresent() ) {
			
			mRepo.deleteById(1L);
			//mRepo.deleteByIdAndSender_Name(1L, "17");
			
			assertTrue ( mRepo.findById(1L).isEmpty() );
			
			mRepo.save(m.get());
		}
			
	}
}

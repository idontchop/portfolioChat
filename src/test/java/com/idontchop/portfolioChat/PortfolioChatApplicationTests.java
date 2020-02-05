package com.idontchop.portfolioChat;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManagerFactory;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.idontchop.portfolioChat.model.Message;
import com.idontchop.portfolioChat.model.MessageThread;
import com.idontchop.portfolioChat.model.User;
import com.idontchop.portfolioChat.repositories.MessageRepository;
import com.idontchop.portfolioChat.repositories.MessageThreadRepository;
import com.idontchop.portfolioChat.repositories.UserRepository;
import com.idontchop.portfolioChat.service.MessageService;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
class PortfolioChatApplicationTests {

	Logger logger = LoggerFactory.getLogger(PortfolioChatApplicationTests.class);
	
	@Autowired
	private MessageRepository mRepo;
	
	@Autowired
	private UserRepository uRepo;
	
	@Autowired
	private MessageThreadRepository mtRepo;
	
	@Autowired
	MessageService ms;
	
	@Test
	@WithMockUser ( username="17" )
	void contextLoads() {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		logger.info(auth.getPrincipal().getClass().toString());
	}
	
	
	
	
	@WithMockUser(username="17",authorities={"USER"}, password = "admin") 
	void addTests() throws IOException {
		
		List<Long> ll = new ArrayList<>();
		ll.add(1L);
		ll.add(12L);
		
		//User u = ms.addUser("21");
		//assertTrue ( u != null );

		//MessageThread mt = ms.addThread(ll);
		//assertTrue ( mt != null);

		

		MessageThread mt = ms.getThreadByTarget(1L, 12L);

		Optional<MessageThread> m = mtRepo.findByMembers("17", "20");
		assertTrue (m.isPresent());
		assertTrue (mt != null);
		
		ms.addMessage("New Message 10", mt.getId(), 1L);
		

		
	}
	
	@Autowired
	private EntityManagerFactory entityManagerFactory;
	
	@Test
	@WithMockUser("17")
	void hibernateDeleteTest () {
		
		
	}
	
	void hibernateTest ( ) {
		
		assertTrue (entityManagerFactory.isOpen());
		
		SessionFactory factory = entityManagerFactory.unwrap(SessionFactory.class);
		
		assertTrue (factory.isOpen());
		
		Session session = factory.openSession();
		
		assertTrue ( session.isConnected());
		
		String hql = "select mt.id from User u join u.messageThreads mt where "
				+ "(u.id = 11 OR u.id = 12) "
				+ "group by mt.id having count(mt.id) = 2";
				//+ "group by u.id having count(u.id) = 2 ";
		
		//String hql = "select members from MessageThread";
		Query q = session.createQuery(hql);
		
		List results = q.list();
		
		assertTrue (results.size() > 0);
		logger.info("".format("%d", results.size()));
		
		assertTrue ( results.get(0) instanceof Long);
		
		hql = "FROM MessageThread mt where exists ("
				+ "select mtt.id from User u join u.messageThreads mtt where "
				+ "mt.id = mtt.id AND "
				+ "(u.id = 14 OR u.id = 2) "
				+ "group by mtt.id having count(mtt.id) = 2)";
		
		q = session.createQuery(hql);
		results = q.list();
		
		assertTrue (results.size() > 0);
		logger.info(String.format("%d", results.size()));
		
		assertTrue ( results.get(0) instanceof MessageThread);
		assertTrue ( ((MessageThread) results.get(0)).getId() == 1 );
	}
	

	
	@Test
	@Order(1)
	@WithMockUser(value = "20")
	void findTests () {

		Optional<MessageThread> mtt = mtRepo.findByMembers("17", "20");
		assertTrue ( mtt.isPresent() );

		Pageable page = PageRequest.of(0, 1);		
		Page<Message> p = mRepo.findAllByCreatedAfter(new Date(), 21L, page);
		assertTrue (p.getTotalElements() == 0);
		

		p = mRepo.findAllByCreatedAfter(new Date (System.currentTimeMillis()-24*60*60*1000), 21L, page);
		assertTrue (p.getTotalPages() == 3);
		assertTrue (p.getTotalElements() == 3);
		
	}

	@Test
	@Order(2)
	@WithMockUser(value = "15")
	void findSecurityTests () {
		
		Pageable page = PageRequest.of(0, 1);		
		Page<Message> p = mRepo.findAllByCreatedAfter(new Date(), 21L, page);
		assertTrue (p.getTotalElements() == 0);

		p = mRepo.findAllByCreatedAfter(new Date (System.currentTimeMillis()-24*60*60*1000), 21L, page);
		assertTrue (p.getTotalPages() == 3);
		assertTrue (p.getTotalElements() == 3);
		
	}
	
	@WithMockUser ( value = "17" )
	void messageRepoTests () {
		
		/*
		Optional<Message> m = mRepo.findById(1L);
		
		assertTrue ( m.isPresent() );
		
		m = mRepo.findById(2L);
		
		assertTrue ( m.isEmpty() );
		*/
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

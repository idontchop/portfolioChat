package com.idontchop.portfolioChat.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Formula;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.idontchop.portfolioChat.service.MessageService;

@Entity
public class MessageThread {
	
	@Id
	@GeneratedValue ( strategy = GenerationType.IDENTITY)
	private long id;
	
	@ManyToMany ( fetch = FetchType.EAGER )
	@Size( min=1 , max=10 )
	@JsonIgnore
	private List<User> members;
	
    @OneToMany(mappedBy = "messageThread", orphanRemoval = true)
    private Collection<Message> messages;
    
    private Date created = new Date();
    
	public MessageThread () {
		
		members = new ArrayList<>();
		
	}
	
	public long getId() {
		return id;
	}
	
	/**
	 * Returns arraylist of longs corresponding to 
	 * members.
	 * 
	 * @return
	 */
	public List<Long> getMemberIds() {
		
		List<Long> ml = new ArrayList<>();
		members.forEach( m -> ml.add(m.getId()));
		
		return ml;
	}
	
	public List<String> getMemberNames() {
		
		List<String> ml = new ArrayList<>();
		members.forEach( m -> ml.add(m.getName()));
		
		return ml;
	}

	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Adds a member to this thread. Will create member list if
	 * empty thread.
	 * 
	 * @param user
	 */
	public void addMember (User user) {
		
		if ( members == null ) {
			members = new ArrayList<>();
		}
		members.add(user);
	}
	
	public boolean hasMembers () {
		if ( members == null || members.size() == 0 )
			return false;
		else return true;
	}
	
	public boolean containsMember (long id) {
		return members.stream().anyMatch( e -> e.getId() == id);
	}
	
	public boolean containsMemeber (String name) {
		return members.stream().anyMatch( e-> e.getName() == name);
	}
	
	public boolean containsMember (User u) {
		return members.stream().anyMatch ( e -> e.getId() == u.getId());
	}
	
	public List<User> getMembers() {
		return members;
	}

	public void setMembers(List<User> members) {
		this.members = members;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}


	
}

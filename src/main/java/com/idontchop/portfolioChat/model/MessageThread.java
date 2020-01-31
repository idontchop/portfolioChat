package com.idontchop.portfolioChat.model;

import java.util.Collection;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;

@Entity
public class MessageThread {

	@Id
	@GeneratedValue ( strategy = GenerationType.IDENTITY)
	private long id;
	
	@OneToMany ( fetch = FetchType.EAGER )
	@Size( min=1 , max=10 )
	private List<User> members;
	
    @OneToMany(mappedBy = "messageThread", orphanRemoval = true)
    private Collection<Message> messages;

	public MessageThread () {
		
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void addMember (User user) {
		members.add(user);
	}
	
	public List<User> getMembers() {
		return members;
	}

	public void setMembers(List<User> members) {
		this.members = members;
	}


	
}

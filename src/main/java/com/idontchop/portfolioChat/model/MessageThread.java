package com.idontchop.portfolioChat.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;

@Entity
public class MessageThread {

	@Id
	@GeneratedValue ( strategy = GenerationType.IDENTITY)
	private long id;
	
	@ManyToMany ( fetch = FetchType.EAGER )
	@Size( min=1 , max=10 )
	private List<User> members;
	
    @OneToMany(mappedBy = "messageThread", orphanRemoval = true)
    private Collection<Message> messages;
    
    private Date created = new Date();

	public MessageThread () {
		
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

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}


	
}

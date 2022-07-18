package com.emesall.news.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true,exclude = {"user","webSites"})
public class UserList extends BaseEntity {

	private static final long serialVersionUID = -1897286705997464492L;

	private String name;
	
	@ManyToOne
	private User user;
	
	@ManyToMany
	@JoinTable(name = "userlist_webSite", joinColumns = @JoinColumn(name = "userlist_id"), inverseJoinColumns = @JoinColumn(name = "webSite_id"))
	private Set<WebSite> webSites = new HashSet<>();

}

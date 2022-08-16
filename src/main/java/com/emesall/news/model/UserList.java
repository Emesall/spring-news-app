package com.emesall.news.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = false,exclude = {"webSites"})
public class UserList extends BaseEntity {

	private static final long serialVersionUID = -1897286705997464492L;

	@NotBlank
	@Pattern(regexp = "^[a-zA-Z0-9]+{8}$")
	@Length(max = 8)
	private String name;
	
	@ManyToOne
	private User user;
	
	//list active as home page for user
	private boolean active;
	
	@ManyToMany
	@JoinTable(name = "userlist_webSite", joinColumns = @JoinColumn(name = "userlist_id"), inverseJoinColumns = @JoinColumn(name = "webSite_id"))
	private Set<WebSite> webSites = new HashSet<>();

}

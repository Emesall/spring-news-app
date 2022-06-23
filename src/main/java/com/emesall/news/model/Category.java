package com.emesall.news.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Category extends BaseEntity {

	private static final long serialVersionUID = -4712248942889122528L;

	private String name;

	@ManyToMany(mappedBy = "categories")
	private Set<Feed> feeds = new HashSet<>();

	@ManyToMany(mappedBy = "categories")
	private Set<User> users = new HashSet<>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "category")
	private Set<Page> pages = new HashSet<>();


}

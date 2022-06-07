package com.emesall.news.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Category extends BaseEntity {

	private static final long serialVersionUID = -4712248942889122528L;

	private String name;
	
	@ManyToMany(mappedBy = "categories")
	private Set<Feed> feeds=new HashSet<>();
	
	@ManyToMany(mappedBy = "categories")
	private Set<User> users=new HashSet<>();
}

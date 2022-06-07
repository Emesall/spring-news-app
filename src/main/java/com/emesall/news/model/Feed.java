package com.emesall.news.model;

import java.net.URI;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Feed extends BaseEntity {

	private static final long serialVersionUID = 3667506579600874791L;

	private String author;
	private Date date_time;
	private String title;
	private String entry;
	private URI url;
	
	@ManyToMany
	@JoinTable(name = "feed_category", joinColumns = @JoinColumn(name = "feed_id"), inverseJoinColumns=@JoinColumn(name = "category_id"))
	private Set<Category> categories=new HashSet<>();
	
	
}

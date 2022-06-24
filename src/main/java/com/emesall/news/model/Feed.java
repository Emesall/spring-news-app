package com.emesall.news.model;

import java.net.URL;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@Entity
@SuperBuilder
@NoArgsConstructor
public class Feed extends BaseEntity {

	private static final long serialVersionUID = 3667506579600874791L;

	private String author;
	private Date date_time;
	private String title;
	@Lob
	private String entry;
	private URL url;
	
	@ManyToMany
	@JoinTable(name = "feed_category", joinColumns = @JoinColumn(name = "feed_id"), inverseJoinColumns=@JoinColumn(name = "category_id"))
	private final Set<Category> categories=new HashSet<>();
	
	
}

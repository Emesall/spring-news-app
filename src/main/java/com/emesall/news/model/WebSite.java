package com.emesall.news.model;

import java.net.URL;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.SortComparator;

import com.emesall.news.comparator.DateComparator;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class WebSite extends BaseEntity {

	private static final long serialVersionUID = -3011921260852414106L;

	private URL url;
	@ManyToOne
	private Category category;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "webSite")
	@SortComparator(DateComparator.class)
	private Set<Feed> feeds = new TreeSet<>();
	

	
}

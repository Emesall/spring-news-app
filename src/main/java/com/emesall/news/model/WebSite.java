package com.emesall.news.model;

import java.net.URL;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

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
	
}

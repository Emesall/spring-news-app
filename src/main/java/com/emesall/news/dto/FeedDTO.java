package com.emesall.news.dto;

import java.net.URI;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import com.emesall.news.model.Category;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedDTO {
	
	private String title;
	private String entry;
	private URI uri;
	private Set<Category> categories=new HashSet<>();
	private String publishedAgo;
	private Instant instant;
	private String imageUrl;
}

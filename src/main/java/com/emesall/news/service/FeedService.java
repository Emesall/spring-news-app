package com.emesall.news.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.emesall.news.model.Feed;
import com.emesall.news.model.WebSite;
import com.rometools.rome.io.FeedException;

public interface FeedService {
	// fetch all feeds from DB
	Page<Feed> fetchAll(Pageable pageable);
	//save feed to DB
	Feed save(Feed feed);
	//find all new RSS feeds in given page
	List<Feed> readNewFeeds(WebSite webSite) throws IOException, FeedException,MalformedURLException;
	//find newest in DB
	Feed findNewest();
	
}

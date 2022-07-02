package com.emesall.news.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Locale;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.emesall.news.dto.FeedDTO;
import com.emesall.news.model.Category;
import com.emesall.news.model.Feed;
import com.emesall.news.model.WebSite;
import com.rometools.rome.io.FeedException;

public interface FeedService {
	// fetch all feeds from DB
	Page<FeedDTO> fetchAll(Pageable pageable);

	// get all feeds from db with given category name
	Page<FeedDTO> fetchByCategory(Category category, Pageable pageable);

	// save feed to DB
	Feed save(Feed feed);

	// find all new RSS feeds in given page
	List<Feed> readNewFeeds(WebSite webSite) throws IOException, FeedException, URISyntaxException;

	// calculate how long ago feed was published and save to DTO object
	void publishedAgo(Page<FeedDTO> feeds, Locale locale, ZoneId zone);
	
	

}

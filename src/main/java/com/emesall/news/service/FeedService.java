package com.emesall.news.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.emesall.news.model.Feed;

public interface FeedService {

	Page<Feed> fetchAll(Pageable pageable);
	Feed save(Feed feed);
}

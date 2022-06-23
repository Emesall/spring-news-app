package com.emesall.news.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.emesall.news.model.Feed;
import com.emesall.news.repository.FeedRepository;

@Service
public class FeedServiceRSS implements FeedService {

	private final FeedRepository feedRepository;

	@Autowired
	public FeedServiceRSS(FeedRepository feedRepository) {
		super();
		this.feedRepository = feedRepository;
	}

	@Override
	public Page<Feed> fetchAll(Pageable pageable) {
		return feedRepository.findAll(pageable);
	}

	@Override
	public Feed save(Feed feed) {

		return feedRepository.save(feed);
	}

}

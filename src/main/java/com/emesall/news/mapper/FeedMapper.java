package com.emesall.news.mapper;

import org.springframework.stereotype.Component;

import com.emesall.news.dto.FeedDTO;
import com.emesall.news.model.Feed;

@Component
public class FeedMapper {

	public FeedDTO feedToDto(Feed feed) {

		FeedDTO feedDto = new FeedDTO();
		feedDto.setEntry(feed.getEntry());
		feedDto.setTitle(feed.getTitle());
		feedDto.setUri(feed.getUri());
		feedDto.setCategories(feed.getCategories());
		feedDto.setInstant(feed.getInstant());
		feedDto.setImageUrl(feed.getImageUrl());
		return feedDto;
	}
}

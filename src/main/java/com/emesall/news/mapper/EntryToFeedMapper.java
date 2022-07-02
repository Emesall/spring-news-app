package com.emesall.news.mapper;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.stereotype.Component;

import com.emesall.news.model.Feed;
import com.rometools.rome.feed.synd.SyndEntry;

@Component
public class EntryToFeedMapper {

	public Feed entryToFeed(SyndEntry entry) throws URISyntaxException {
		Feed feed = new Feed();

		feed.setTitle(entry.getTitle());
		feed.setAuthor(entry.getAuthor());
		feed.setInstant(entry.getPublishedDate().toInstant());
		feed.setUri(new URI(entry.getLink()));
		feed.setEntry(entry.getDescription().getValue());

		return feed;
	}
}

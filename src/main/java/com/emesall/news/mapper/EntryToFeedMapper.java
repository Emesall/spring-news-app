package com.emesall.news.mapper;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.jdom2.Element;
import org.springframework.stereotype.Component;

import com.emesall.news.model.Feed;
import com.rometools.rome.feed.synd.SyndEnclosure;
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

		String imageUrl = getImageUrl(entry.getEnclosures(), entry.getForeignMarkup());
		if (imageUrl != null) {
			feed.setImageUrl(imageUrl);
		}

		return feed;
	}

	private String getImageUrl(List<SyndEnclosure> enclosures, List<Element> markups) {
		if (enclosures != null) {
			for (SyndEnclosure enclosure : enclosures) {
				if (enclosure.getType() != null && enclosure.getType().contains("image")) {

					return enclosure.getUrl();
				}
			}

		}
		if (markups != null) {
			for (Element markup : markups) {
				if (markup.getAttribute("url") != null) {

					return markup.getAttribute("url").getValue();
				}
			}
		}
		return null;
	}
}

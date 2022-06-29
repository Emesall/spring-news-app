package com.emesall.news.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.emesall.news.model.Category;
import com.emesall.news.model.Feed;
import com.emesall.news.model.WebSite;
import com.emesall.news.repository.FeedRepository;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

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

	@Override
	public List<Feed> readNewFeeds(WebSite webSite) throws IOException, FeedException, URISyntaxException {
		SyndFeedInput input = new SyndFeedInput();
		SyndFeed feed = input.build(new XmlReader(webSite.getUrl()));
		ArrayList<Feed> feeds = new ArrayList<>();
		for (Object o : feed.getEntries()) {
			SyndEntry entry = (SyndEntry) o;
			if (isFeedNew(webSite, entry.getPublishedDate())) {
				Feed parsedFeed = new Feed();

				parsedFeed.setTitle(entry.getTitle());
				parsedFeed.setAuthor(entry.getAuthor());
				parsedFeed.setDateTime(entry.getPublishedDate());
				parsedFeed.setUri(new URI(entry.getLink()));
				parsedFeed.setEntry(entry.getDescription().getValue());
				parsedFeed.getCategories().add(webSite.getCategory());
				parsedFeed.setWebSite(webSite);
				feeds.add(parsedFeed);
			}

		}

		return feeds;
	}

	// check if fetched entry is new
	private boolean isFeedNew(WebSite webSite, Date date) {

		Set<Feed> feeds = webSite.getFeeds();
		// if set is empty, there is no feed at all so webSite must be new
		if (feeds.isEmpty())
			return true;
		// find latest feed (first in Set)
		Date latestDate = feeds.iterator().next().getDateTime();

		if (date.compareTo(latestDate) > 0)
			return true; // feed is newer than the latest in DB

		return false; // feed is already in DB
	}

	@Override
	public Page<Feed> fetchByCategory(Category category, Pageable pageable) {
		List<Feed> feeds = category.getFeeds().stream().toList();
		int start = (int) pageable.getOffset();
		int end = Math.min((start + pageable.getPageSize()), feeds.size());
		Page<Feed> page = new PageImpl<Feed>(feeds.subList(start, end), pageable, feeds.size());

		return page;
	}

}

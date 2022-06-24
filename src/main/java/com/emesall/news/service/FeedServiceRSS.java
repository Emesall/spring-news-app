package com.emesall.news.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
	public Feed findNewest() {
		return feedRepository.findAllOrderByDateTime().get(0);
	}
	
	@Override
	public List<Feed> readNewFeeds(WebSite webSite) throws IOException, FeedException, MalformedURLException {
		SyndFeedInput input = new SyndFeedInput();
		SyndFeed feed = input.build(new XmlReader(webSite.getUrl()));
		ArrayList<Feed> feeds = new ArrayList<>();
		for (Object o : feed.getEntries()) {
			SyndEntry entry = (SyndEntry) o;
			if (checkIfNew(entry.getPublishedDate())) {
				Feed parsedFeed = new Feed();

				parsedFeed.setTitle(entry.getTitle());
				parsedFeed.setAuthor(entry.getAuthor());
				parsedFeed.setDate_time(entry.getPublishedDate());
				parsedFeed.setUrl(new URL(entry.getLink()));
				parsedFeed.setEntry(entry.getDescription().getValue());
				parsedFeed.getCategories().add(webSite.getCategory());
				feeds.add(parsedFeed);
			}

		}

		return feeds;
	}

	private boolean checkIfNew(Date date) {
		// find newest in DB
		Feed f = findNewest();
		if (date.compareTo(f.getDate_time())>0)
			return true;

		return false;
	}

	

}

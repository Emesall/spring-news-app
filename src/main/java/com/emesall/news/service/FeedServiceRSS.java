package com.emesall.news.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.emesall.news.dto.FeedDTO;
import com.emesall.news.mapper.EntryToFeedMapper;
import com.emesall.news.mapper.FeedMapper;
import com.emesall.news.model.Category;
import com.emesall.news.model.Feed;
import com.emesall.news.model.WebSite;
import com.emesall.news.repository.FeedRepository;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import net.time4j.PrettyTime;

@Service
public class FeedServiceRSS implements FeedService {

	private final FeedRepository feedRepository;
	private final EntryToFeedMapper entryToFeed;
	private final FeedMapper feedMapper;

	@Autowired
	public FeedServiceRSS(FeedRepository feedRepository, EntryToFeedMapper entryToFeed, FeedMapper feedMapper) {
		super();
		this.feedRepository = feedRepository;
		this.entryToFeed = entryToFeed;
		this.feedMapper = feedMapper;
	}

	@Override
	public Page<FeedDTO> fetchAll(Pageable pageable) {
		return feedRepository.findAll(pageable).map(f -> feedMapper.feedToDto(f));
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
			if (isFeedNew(webSite, entry.getPublishedDate().toInstant())) {
				Feed parsedFeed = entryToFeed.entryToFeed(entry);
				parsedFeed.getCategories().add(webSite.getCategory());
				parsedFeed.setWebSite(webSite);
				feeds.add(parsedFeed);
			}

		}

		return feeds;
	}

	// check if fetched entry is new
	private boolean isFeedNew(WebSite webSite, Instant date) {

		Set<Feed> feeds = webSite.getFeeds();
		// if set is empty, there is no feed at all so webSite must be new
		if (feeds.isEmpty())
			return true;
		// find latest feed (first in Set)
		Instant latestDate = feeds.iterator().next().getInstant();

		if (date.compareTo(latestDate) > 0)
			return true; // feed is newer than the latest in DB

		return false; // feed is already in DB
	}

	@Override
	public Page<FeedDTO> fetchByCategory(Category category, Pageable pageable) {
		List<FeedDTO> feeds = category.getFeeds().stream().map(f -> feedMapper.feedToDto(f)).toList();
		int start = (int) pageable.getOffset();
		int end = Math.min((start + pageable.getPageSize()), feeds.size());
		Page<FeedDTO> page = new PageImpl<FeedDTO>(feeds.subList(start, end), pageable, feeds.size());

		return page;
	}

	@Override
	public void publishedAgo(Page<FeedDTO> feeds, Locale locale, ZoneId zone) {
		feeds.forEach(f -> f.setPublishedAgo(PrettyTime.of(locale).printRelative(f.getInstant(), zone)));
		

	}

}

package com.emesall.news.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.emesall.news.dto.FeedDTO;
import com.emesall.news.mapper.EntryToFeedMapper;
import com.emesall.news.mapper.FeedMapper;
import com.emesall.news.model.Category;
import com.emesall.news.model.Feed;
import com.emesall.news.model.UserList;
import com.emesall.news.model.WebSite;
import com.emesall.news.repository.FeedRepository;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import net.time4j.PrettyTime;

@Service
public class FeedService {

	private final FeedRepository feedRepository;
	private final EntryToFeedMapper entryToFeed;
	private final FeedMapper feedMapper;

	@Autowired
	public FeedService(FeedRepository feedRepository, EntryToFeedMapper entryToFeed, FeedMapper feedMapper) {
		super();
		this.feedRepository = feedRepository;
		this.entryToFeed = entryToFeed;
		this.feedMapper = feedMapper;
	}

	public Page<FeedDTO> fetchAll(Pageable pageable) {
		return feedRepository.findAll(pageable).map(f -> feedMapper.feedToDto(f));
	}

	
	
	public Page<FeedDTO> fetchFromList(UserList userList, Pageable pageable) {
		List<WebSite> websites = userList.getWebSites().stream().collect(Collectors.toList());
		return feedRepository.findByWebSiteIn(websites, pageable).map(f -> feedMapper.feedToDto(f));
	}

	public Feed save(Feed feed) {

		return feedRepository.save(feed);
	}

	public List<Feed> readNewFeeds(WebSite webSite) throws IOException, FeedException, URISyntaxException {
		SyndFeedInput input = new SyndFeedInput();
		SyndFeed feed = input.build(new XmlReader(webSite.getUrl()));
		ArrayList<Feed> feeds = new ArrayList<>();
		for (Object o : feed.getEntries()) {
			SyndEntry entry = (SyndEntry) o;
			if (isFeedNew(webSite, entry)) {
			
				Feed parsedFeed = entryToFeed.entryToFeed(entry);
				parsedFeed.getCategories().add(webSite.getCategory());
				parsedFeed.setWebSite(webSite);
				feeds.add(parsedFeed);
			}

		}

		return feeds;
	}

	// check if fetched entry is new
	private boolean isFeedNew(WebSite webSite, SyndEntry entry) {

		Set<Feed> feeds = feedRepository.findByWebSite(webSite,Sort.by("instant").descending());
		
		for(Feed feed:feeds) {

			if(feed.getUri().toString().equals(entry.getLink()))
			{
				return false; // feed is already in DB
			}
		}
		return true; // feed is new
	}


	private Page<Feed> getFeedsByCategory(Category category, Pageable pageable) {
		List<Category> categories=new ArrayList<Category>();
		categories.add(category);
		return feedRepository.findByCategoriesIn(categories, pageable);
	}
	
	public Page<FeedDTO> fetchByCategory(Category category, Pageable pageable) {
		return getFeedsByCategory(category, pageable).map(feed->feedMapper.feedToDto(feed));
	}


	public void publishedAgo(Page<FeedDTO> feeds, Locale locale, ZoneId zone) {
		feeds.forEach(f -> f.setPublishedAgo(PrettyTime.of(locale).printRelative(f.getInstant(), zone)));

	}
	
	public List<Feed> findOlderNews(Instant instant){
		return feedRepository.findByInstantLessThan(instant);
	}
	
	public void deleteNews(List<Feed> feeds) {
		feeds.forEach(feed->feedRepository.delete(feed));
	}

}

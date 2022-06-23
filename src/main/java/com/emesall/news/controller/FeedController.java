package com.emesall.news.controller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.emesall.news.model.Feed;
import com.emesall.news.service.FeedService;
import com.emesall.news.service.PageService;
import com.rometools.rome.feed.synd.SyndCategory;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

@Controller
public class FeedController {

	private final FeedService feedService;
	private final PageService pageService;

	@Autowired
	public FeedController(FeedService feedService, PageService pageService) {
		super();
		this.feedService = feedService;
		this.pageService = pageService;
	}

	@GetMapping("/fetch")
	public String fetchFeed() {

		try {
			Feed feed=readAndSaveFeed("https://api.foxsports.com/v2/content/optimized-rss?partnerKey=MB0Wehpmuj2lUhuRhQaafhBjAJqaPU244mlTDK1i&size=30");
			feedService.save(feed);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/";
	}

	private Feed readAndSaveFeed(String feedUrl) throws IOException, FeedException,URISyntaxException {
		URL feedSource = new URL(feedUrl);
		SyndFeedInput input = new SyndFeedInput();
		SyndFeed feed = input.build(new XmlReader(feedSource));
		Feed parsedFeed = new Feed();
		for (Object o : feed.getEntries()) {
			SyndEntry entry = (SyndEntry) o;

			parsedFeed.setTitle(entry.getTitle());
			parsedFeed.setAuthor(entry.getAuthor());
			parsedFeed.setDate_time(entry.getPublishedDate());
			parsedFeed.setUrl(new URI(entry.getLink()));
			for (SyndCategory cat : entry.getCategories()) {
				System.out.println(cat.getName());
			}

		}

		return parsedFeed;
	}
}

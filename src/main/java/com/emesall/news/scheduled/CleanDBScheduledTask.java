package com.emesall.news.scheduled;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.emesall.news.model.Feed;
import com.emesall.news.service.FeedService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CleanDBScheduledTask {

	private final static int TIME = 24 * 3600000; // 24h to ms
	private final static int EXPIRATION_TIME = 5 * 24 * 3600000; // 5days to ms
	private final FeedService feedService;

	@Autowired
	public CleanDBScheduledTask(FeedService feedService) {
		super();
		this.feedService = feedService;
	}

	@Scheduled(fixedRate = TIME)
	public void scheduledCleanDB() {

		List<Feed> feeds = feedService.findOlderNews(Instant.now().minusMillis(EXPIRATION_TIME)); // fetch older news
																									// than that time
		log.debug("Finding old news: " + feeds.size());
		if (feeds != null) {
			feedService.deleteNews(feeds);
			log.debug("Deleting old news: " + feeds.size());
		}
	}

}

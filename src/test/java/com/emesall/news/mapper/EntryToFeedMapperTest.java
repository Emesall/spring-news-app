package com.emesall.news.mapper;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.emesall.news.model.Feed;
import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndContentImpl;
import com.rometools.rome.feed.synd.SyndEnclosure;
import com.rometools.rome.feed.synd.SyndEnclosureImpl;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndEntryImpl;

class EntryToFeedMapperTest {

	private static final String ENTRY = "entry";
	private static final String IMAGE_TYP = "image";
	private static final String URL = "https://www.skysports.com/rss/12040";
	private static final String TITLE = "title";
	private static final String AUTHOR = "Author";
	SyndEntry entry;
	EntryToFeedMapper mapper;
	Date date;
	

	@BeforeEach
	void setUp() throws Exception {
		mapper = new EntryToFeedMapper();
		entry = new SyndEntryImpl();
		date=Calendar.getInstance().getTime();
		
		List<SyndEnclosure> enclosures=new ArrayList<>();
		SyndEnclosure enclosure=new SyndEnclosureImpl();
		enclosure.setUrl(URL);
		enclosure.setType(IMAGE_TYP);
		enclosures.add(enclosure);
		SyndContent content= new SyndContentImpl();
		content.setValue(ENTRY);
		
		entry.setAuthor(AUTHOR);
		entry.setTitle(TITLE);
		entry.setPublishedDate(date);
		entry.setLink(URL);
		entry.setEnclosures(enclosures);
		entry.setDescription(content);
		
	}

	@Test
	void testEntryToFeed() throws URISyntaxException {

		Feed feed = mapper.entryToFeed(entry);
		assertEquals(feed.getAuthor(), AUTHOR);
		assertEquals(feed.getTitle(), TITLE);
		assertEquals(feed.getInstant(), date.toInstant());
		assertEquals(feed.getUri(), new URI(URL));
		assertEquals(feed.getImageUrl(), URL);
		assertEquals(feed.getEntry(), ENTRY);
	}
	
	

}

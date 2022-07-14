package com.emesall.news.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.emesall.news.dto.FeedDTO;
import com.emesall.news.mapper.EntryToFeedMapper;
import com.emesall.news.mapper.FeedMapper;
import com.emesall.news.model.Category;
import com.emesall.news.model.Feed;
import com.emesall.news.repository.FeedRepository;

class FeedServiceTest {

	private static final String CAT_NAME = "Sport";
	private static final String TITLE = "title";
	private static final long ID = 1L;
	private static final String ENTRY = "entry";
	private static final int SIZE = 5;
	private static final int PAGE = 0;
	FeedService feedService;
	@Mock
	FeedRepository feedRepository;
	@Mock
	EntryToFeedMapper entryToFeed;
	@Mock
	FeedMapper feedMapper;

	Feed feed;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
		feedService = new FeedService(feedRepository, entryToFeed, feedMapper);
		feed = Feed.builder().entry(ENTRY).id(ID).title(TITLE).build();
	}

	@Test
	void testFetchAll() {
		// given
		Pageable pageable = PageRequest.of(PAGE, SIZE);
		List<Feed> feeds = new ArrayList<>();
		feeds.add(feed);
		// feedDTO
		FeedDTO feedDTO = new FeedDTO();
		feedDTO.setEntry(ENTRY);
		feedDTO.setTitle(TITLE);

		Page<Feed> page = new PageImpl<>(feeds, pageable, feeds.size());
		when(feedRepository.findAll(any(Pageable.class))).thenReturn(page);
		when(feedMapper.feedToDto(any(Feed.class))).thenReturn(feedDTO);

		// when
		Page<FeedDTO> fetchedFeed = feedService.fetchAll(pageable);
		// then

		assertNotNull(fetchedFeed);
		verify(feedRepository, times(1)).findAll(any(Pageable.class));
		assertEquals(ENTRY, fetchedFeed.getContent().get(0).getEntry());
		assertEquals(TITLE, fetchedFeed.getContent().get(0).getTitle());
		assertEquals(1, fetchedFeed.getContent().size());
		assertEquals(PAGE, fetchedFeed.getPageable().getPageNumber());

	}

	@Test
	void testSave() {
		// given
		when(feedRepository.save(any(Feed.class))).thenReturn(feed);
		// when
		Feed foundFeed = feedService.save(feed);
		// then
		assertNotNull(feed);
		verify(feedRepository, times(1)).save(any(Feed.class));
		assertEquals(ID, foundFeed.getId());
		assertEquals(ENTRY, foundFeed.getEntry());
		assertEquals(TITLE, foundFeed.getTitle());
	}

	@Test
	void testReadNewFeeds() {
		// given

		// when

		// then
	}

	@Test
	void testFetchByCategory() {
		// given

		Pageable pageable = PageRequest.of(PAGE, SIZE);
		List<Feed> feeds = new ArrayList<>();
		feeds.add(feed);
		// feedDTO
		FeedDTO feedDTO = new FeedDTO();
		feedDTO.setEntry(ENTRY);
		feedDTO.setTitle(TITLE);
		// category
		Category category = new Category();
		category.setId(ID);
		category.setName(CAT_NAME);
		category.setFeeds(feeds.stream().collect(Collectors.toSet()));

		when(feedMapper.feedToDto(any(Feed.class))).thenReturn(feedDTO);

		// when
		Page<FeedDTO> fetchedFeed = feedService.fetchByCategory(category, pageable);
		// then

		assertNotNull(fetchedFeed);
		assertEquals(ENTRY, fetchedFeed.getContent().get(0).getEntry());
		assertEquals(TITLE, fetchedFeed.getContent().get(0).getTitle());
		assertEquals(1, fetchedFeed.getContent().size());
		assertEquals(PAGE, fetchedFeed.getPageable().getPageNumber());
	}

	@Test
	void testPublishedAgo() {
		// given
		Pageable pageable = PageRequest.of(PAGE, SIZE);
		List<FeedDTO> feeds = new ArrayList<>();
		FeedDTO feedDTO = new FeedDTO();
		feedDTO.setEntry(ENTRY);
		feedDTO.setTitle(TITLE);
		feedDTO.setInstant(Instant.now());
		feeds.add(feedDTO);
		Page<FeedDTO> page = new PageImpl<>(feeds, pageable, feeds.size());
		// when
		feedService.publishedAgo(page, Locale.getDefault(), ZoneId.systemDefault());
		// then
		assertNotNull(page.getContent().get(0).getPublishedAgo());

	}

}

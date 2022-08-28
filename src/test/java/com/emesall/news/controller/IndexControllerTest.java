package com.emesall.news.controller;

import static org.hamcrest.CoreMatchers.equalToObject;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.emesall.news.dto.FeedDTO;
import com.emesall.news.model.Category;
import com.emesall.news.model.User;
import com.emesall.news.service.CategoryService;
import com.emesall.news.service.FeedService;
import com.emesall.news.service.UserListService;

@EnableConfigurationProperties
class IndexControllerTest {

	private static final int SIZE = 5;

	private static final int PAGE = 1;

	private static final String TITLE = "title";

	private static final String ENTRY = "entry";

	private static final String TITLE2 = "title2";

	private static final String ENTRY2 = "entry2";

	MockMvc mockMvc;

	@InjectMocks
	IndexController indexController;

	@Mock
	FeedService feedService;

	FeedDTO feedDTO1;
	FeedDTO feedDTO2;
	
	@Mock
	CategoryService categoryService;
	@Mock
	UserListService listService;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(indexController).build();
		feedDTO1 = new FeedDTO();
		feedDTO1.setEntry(ENTRY);
		feedDTO1.setTitle(TITLE);

		feedDTO2 = new FeedDTO();
		feedDTO2.setEntry(ENTRY2);
		feedDTO2.setTitle(TITLE2);

	}

	@Test
	void testIndex() throws Exception {
		// given
		Pageable pageable = PageRequest.of(PAGE, SIZE);
		List<FeedDTO> feeds = new ArrayList<>();
		feeds.add(feedDTO1);
		feeds.add(feedDTO2);
		Page<FeedDTO> page = new PageImpl<>(feeds, pageable, feeds.size());
		when(feedService.fetchAll(any(Pageable.class))).thenReturn(page);
		when(listService.returnActiveList(any(User.class))).thenReturn(Optional.empty());

		indexController.setPageSize(8);
		// then
		mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(view().name("index"))
				.andExpect(model().attributeExists("currentPage"))
				.andExpect(model().attributeExists("totalPages"))
				.andExpect(model().attributeExists("results"))
				.andExpect(model().attribute("results", equalToObject(feeds)));

		verify(feedService, times(1)).fetchAll(any(Pageable.class));
		verify(feedService, times(0)).fetchByCategory(any(Category.class), any(Pageable.class));
	}

	@Test
	void testIndexCategory() throws Exception {
		// given
		Pageable pageable = PageRequest.of(PAGE, SIZE);
		List<FeedDTO> feeds = new ArrayList<>();
		feeds.add(feedDTO1);
		feeds.add(feedDTO2);
		Page<FeedDTO> page = new PageImpl<>(feeds, pageable, feeds.size());
		Category category=new Category();
		category.setName("Sport");
		
		when(categoryService.findByName(any(String.class))).thenReturn(category);
		when(feedService.fetchByCategory(any(Category.class),any(Pageable.class))).thenReturn(page);

		indexController.setPageSize(8);
		// then
		mockMvc.perform(get("/")
				.param("tag", "Sport"))
				.andExpect(status().isOk())
				.andExpect(view().name("index"))
				.andExpect(model().attributeExists("currentPage"))
				.andExpect(model().attributeExists("totalPages"))
				.andExpect(model().attributeExists("results"))
				.andExpect(model().attribute("results", equalToObject(feeds)));

		verify(feedService, times(0)).fetchAll(any(Pageable.class));
		verify(feedService, times(1)).fetchByCategory(any(Category.class), any(Pageable.class));
	}

}

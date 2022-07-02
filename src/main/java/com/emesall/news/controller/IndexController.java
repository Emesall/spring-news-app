package com.emesall.news.controller;

import java.net.http.HttpRequest;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.emesall.news.model.Category;
import com.emesall.news.model.Feed;
import com.emesall.news.service.CategoryService;
import com.emesall.news.service.FeedService;

import lombok.Setter;

@Controller
@Setter
@ConfigurationProperties(prefix = "feed")
public class IndexController {

	private final FeedService feedService;
	private final CategoryService categoryService;
	private int pageSize;

	@Autowired
	public IndexController(FeedService feedService, CategoryService categoryService) {
		super();
		this.feedService = feedService;
		this.categoryService = categoryService;
	}

	@GetMapping("/")
	public String index(Model model, @RequestParam(required = false, value = "tag") String cat_name,
			@RequestParam(required = false, value = "page") Integer page, HttpServletRequest request) {

		if (page == null) {
			page = 1;
		}
		Page<Feed> results;
		Pageable pageable = PageRequest.of(page - 1, pageSize);
		// no category given
		if (cat_name == null) {
			results = feedService.fetchAll(pageable);
		} else {
			Category category = categoryService.findByName(cat_name);
			results = feedService.fetchByCategory(category, pageable);
		}
		List<String> timeAgo = new ArrayList<>();
		for (int i = 0; i < results.getContent().size(); i++) {
			timeAgo.add(feedService.timeAgo(results.getContent().get(i).getInstant(), request.getLocale(),
					ZoneId.systemDefault()));

		}
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", results.getTotalPages());
		model.addAttribute("results", results.getContent());
		model.addAttribute("timeAgo", timeAgo);

		return "index";
	}

}

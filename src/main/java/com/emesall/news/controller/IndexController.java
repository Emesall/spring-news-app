package com.emesall.news.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.emesall.news.model.Feed;
import com.emesall.news.service.FeedService;

import lombok.Setter;

@Controller
@Setter
@ConfigurationProperties(prefix="feed")
public class IndexController {

	private final FeedService feedService;
	private int pageSize;

	@Autowired
	public IndexController(FeedService feedService) {
		super();
		this.feedService = feedService;
	}

	@GetMapping("/")
	public String index(Model model, @RequestParam(required = false) Integer page) {

		if (page == null) {
			page = 1;
		}
		Page<Feed> results = feedService.fetchAll(PageRequest.of(page-1, pageSize));
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", results.getTotalPages());
		model.addAttribute("results", results.getContent());
		
		return "index";
	}

}

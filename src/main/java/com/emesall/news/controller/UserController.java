package com.emesall.news.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.emesall.news.model.UserList;
import com.emesall.news.service.UserService;
import com.emesall.news.service.WebSiteService;

@Controller
public class UserController {

	private final UserService userService;
	private final WebSiteService webSiteService;

	@Autowired
	public UserController(UserService userService, WebSiteService webSiteService) {
		super();
		this.userService = userService;
		this.webSiteService = webSiteService;
	}

	@GetMapping("/settings")
	public String settings() {

		return "user/settings";
	}

	@GetMapping("/list/new")
	public String initNewList(Model model) {
		model.addAttribute("list", new UserList());
		model.addAttribute("map",webSiteService.orderWebSitesByCategory());

		return "user/list";
	}

	@PostMapping("/list/new")
	public String processNewList() {

		return "redirect:/settings";
	}

}

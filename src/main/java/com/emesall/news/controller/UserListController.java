package com.emesall.news.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.emesall.news.exception.NotFoundException;
import com.emesall.news.model.User;
import com.emesall.news.model.UserList;
import com.emesall.news.service.UserService;
import com.emesall.news.service.WebSiteService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class UserListController {

	private final UserService userService;
	private final WebSiteService webSiteService;

	@Autowired
	public UserListController(UserService userService, WebSiteService webSiteService) {
		super();
		this.userService = userService;
		this.webSiteService = webSiteService;
		
	}

	

	@GetMapping("/list/new")
	public String initNewList(Model model) {
		model.addAttribute("list", new UserList());
		model.addAttribute("map", webSiteService.orderWebSitesByCategory());

		return "user/list";
	}

	@PostMapping("/list/update")
	public String processList(@ModelAttribute("list") UserList userlist, @AuthenticationPrincipal User user) {

		log.debug("Saving/updating list in database..");
		userlist.setUser(user);
		userService.saveUserList(userlist);
		return "redirect:/settings";
	}

	@GetMapping("/list/{id}/edit")
	public String initEditList(Model model, @PathVariable("id") Long listId, @AuthenticationPrincipal User user) {
		UserList userList = userService.findListById(listId);
		if (!userList.getUser().equals(user)) {
			throw new NotFoundException("List not found");
		}
		model.addAttribute("list", userList);
		model.addAttribute("map", webSiteService.orderWebSitesByCategory());

		return "user/list";
	}
	
	@GetMapping("/list/{id}/delete")
	public String deleteList(@PathVariable Long id, Model model) {
		log.debug("Deleting list with ID: " + id);
		userService.deleteUserListById(id);
		return "redirect:/settings";
	}
	
	
}

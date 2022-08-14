package com.emesall.news.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.emesall.news.exception.NotFoundException;
import com.emesall.news.model.Category;
import com.emesall.news.model.User;
import com.emesall.news.model.UserList;
import com.emesall.news.model.WebSite;
import com.emesall.news.service.UserListService;
import com.emesall.news.service.WebSiteService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class UserListController {

	private final UserListService userListService;
	private final WebSiteService webSiteService;

	@Autowired
	public UserListController(UserListService userListService, WebSiteService webSiteService) {
		super();
		this.userListService = userListService;
		this.webSiteService = webSiteService;

	}

	@ModelAttribute("map")
	public MultiValueMap<Category, WebSite> getWebSites() {
		return webSiteService.orderWebSitesByCategory();
	}

	@GetMapping("/list/new")
	public String initNewList(Model model) {
		model.addAttribute("list", new UserList());

		return "user/list";
	}

	@PostMapping("/list/update")
	public String processList(@ModelAttribute("list") @Valid UserList userlist, BindingResult bindingResult,
			@AuthenticationPrincipal User user, Model model) {

		userlist.setUser(user);
		// check if there is already list with the same name
		if (userListService.checkIfUserListExists(user, userlist)) {
			bindingResult.rejectValue("name", "TheSame.list.name");
		}

		if (bindingResult.hasErrors()) {
			model.addAttribute("list", userlist);
			return "user/list";
		}
		log.debug("Saving/updating list in database..");
		userListService.saveUserList(userlist);
		return "redirect:/settings";
	}

	@GetMapping("/list/{id}/edit")
	public String initEditList(Model model, @PathVariable("id") Long listId, @AuthenticationPrincipal User user) {
		UserList userList = userListService.findListById(listId);
		if (!userList.getUser().equals(user)) {
			throw new NotFoundException("List not found");
		}
		model.addAttribute("list", userList);

		return "user/list";
	}

	@PostMapping("/list/{id}/delete")
	public String deleteList(@PathVariable Long id, Model model) {
		log.debug("Deleting list with ID: " + id);
		userListService.deleteUserListById(id);
		return "redirect:/settings";
	}

	// active list is shown on home page
	@GetMapping("/list/{id}/setAsActive")
	public String setListAsActive(Model model, @PathVariable("id") Long listId, @AuthenticationPrincipal User user) {
		UserList userList = userListService.findListById(listId);
		if (!userList.getUser().equals(user)) {
			throw new NotFoundException("List not found");
		}
		userListService.setListAsActive(userList, user);
		userListService.saveUserList(userList);
		log.debug(user.getEmail() + " list " + userList.getId() + " set as active.");
		return "redirect:/settings";
	}

	// deactive list is shown on home page
	@GetMapping("/list/{id}/setAsInactive")
	public String setListAsInActive(Model model, @PathVariable("id") Long listId, @AuthenticationPrincipal User user) {
		UserList userList = userListService.findListById(listId);
		if (!userList.getUser().equals(user)) {
			throw new NotFoundException("List not found");
		}
		userList.setActive(false);
		userListService.saveUserList(userList);
		log.debug(user.getEmail() + " list " + userList.getId() + " set as inactive.");
		return "redirect:/settings";
	}

}

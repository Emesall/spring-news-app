package com.emesall.news.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.emesall.news.model.User;
import com.emesall.news.model.UserList;
import com.emesall.news.service.UserListService;

@ControllerAdvice
public class NavController {

	private final UserListService userListService;

	@Autowired
	public NavController(UserListService userListService) {
		super();
		this.userListService = userListService;
	}
	
	
	
	@ModelAttribute("userLists")
	public Set<UserList> getLists(@AuthenticationPrincipal User user) {
		if (user != null) {
			return userListService.findListByUser(user);
		}
		return null;
	}

}

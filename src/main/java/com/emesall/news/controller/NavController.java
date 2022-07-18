package com.emesall.news.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.emesall.news.model.UserList;
import com.emesall.news.service.UserService;

@ControllerAdvice
public class NavController {


	private final UserService userService;


	
	@Autowired
	public NavController(UserService userService) {
		super();
		this.userService = userService;
	}


	@ModelAttribute("userLists")
	public Set<UserList> getLists() {
		return userService.findAllLists();
	}

	
}

package com.emesall.news.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {


	@GetMapping("/settings")
	public String settings() {



		return "user/settings";
	}
	
	
}

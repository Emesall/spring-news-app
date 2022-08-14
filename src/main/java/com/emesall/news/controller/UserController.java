package com.emesall.news.controller;

import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.emesall.news.dto.ChangeNameForm;
import com.emesall.news.dto.ChangePasswordForm;
import com.emesall.news.mapper.ChangeNameFormMapper;
import com.emesall.news.model.User;
import com.emesall.news.model.UserList;
import com.emesall.news.service.UserListService;
import com.emesall.news.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class UserController {

	private static final String USER_SETTINGS = "user/settings";
	private final UserService userService;
	private final UserListService userListService;
	private final ChangeNameFormMapper nameFormMapper;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	public UserController(UserService userService, ChangeNameFormMapper nameFormMapper,
			PasswordEncoder passwordEncoder,UserListService userListService) {
		super();
		this.userService = userService;
		this.nameFormMapper = nameFormMapper;
		this.passwordEncoder = passwordEncoder;
		this.userListService = userListService;
	}

	@ModelAttribute("lists")
	public Set<UserList> findLists(@AuthenticationPrincipal User user) {
		return  userListService.findListByUser(user);
	}
	@ModelAttribute("nameForm")
	public ChangeNameForm getNameForm(@AuthenticationPrincipal User user) {
		return  nameFormMapper.userToChangeNameForm(user);
	}
	@ModelAttribute("passwordForm")
	public ChangePasswordForm getPasswordForm(@AuthenticationPrincipal User user) {
		return  new ChangePasswordForm();
	}

	
	@GetMapping("/settings")
	public String settings() {


		return USER_SETTINGS;
	}

	@PostMapping("/user/updateName")
	public String updateName(@ModelAttribute("nameForm") ChangeNameForm changeNameForm,
			@AuthenticationPrincipal User user) {

		log.debug("Updating firstname/lastname..");
		user.setFirstName(changeNameForm.getFirstName());
		user.setLastName(changeNameForm.getLastName());
		userService.saveUser(user);
		return "redirect:/settings?nameUpdated";
	}

	@PostMapping("/changePassword")
	public String processChangePassword(@ModelAttribute("passwordForm") @Valid ChangePasswordForm passwordForm,
			BindingResult bindingResult, @AuthenticationPrincipal User user) {

		// if changed password is the same as previous one

		if (passwordEncoder.matches(passwordForm.getPassword(), user.getPassword())) {
			bindingResult.rejectValue("password", "TheSame.passwordForm.password");

		}

		if (bindingResult.hasErrors()) {
			return USER_SETTINGS;

		}
		user.setPassword(passwordEncoder, passwordForm.getPassword());
		userService.saveUser(user);

		return "redirect:/settings?passChanged";

	}

}

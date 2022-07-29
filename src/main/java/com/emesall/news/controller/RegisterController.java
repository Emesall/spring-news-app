package com.emesall.news.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.emesall.news.dto.RegistrationForm;
import com.emesall.news.mapper.RegisterFormToUser;
import com.emesall.news.model.User;
import com.emesall.news.service.UserService;

@Controller
public class RegisterController {

	
	private static final String USER_REGISTER_FORM = "user/registerForm";
	private final RegisterFormToUser formToUser;
	private final PasswordEncoder encoder;
	private final UserService userService;
	
	
	
	@Autowired
	public RegisterController(RegisterFormToUser formToUser, PasswordEncoder encoder,UserService userService) {
		super();
		this.formToUser = formToUser;
		this.encoder = encoder;
		this.userService=userService;
	}

	// handle registration of owner
	@GetMapping("/register")
	public String registerForm(Model model) {
		model.addAttribute("registerForm", new RegistrationForm());
		return USER_REGISTER_FORM;
	}

	@PostMapping("/register")
	public String processRegistration(@ModelAttribute("registerForm") @Valid RegistrationForm form,
			BindingResult bindingResult, HttpServletRequest request, Model model) {

		// transform registerForm to user
		User user=formToUser.registerFormToUser(form, encoder);
		// check if user exists in database
		if (userService.checkIfUserExists(user))
			bindingResult.rejectValue("username", "username.exists");
		// check if there are some validations errors
		if (bindingResult.hasErrors())
			return USER_REGISTER_FORM;
		else {

			userService.saveUser(user);
		
			// save user

			return "redirect:/login?registered";
		}

	}
}

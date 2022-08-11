package com.emesall.news.controller;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.emesall.news.dto.RegistrationForm;
import com.emesall.news.events.OnRegistrationCompleteEvent;
import com.emesall.news.mapper.RegisterFormToUserMapper;
import com.emesall.news.model.User;
import com.emesall.news.model.token.VerificationToken;
import com.emesall.news.service.UserService;
import com.emesall.news.service.token.VerificationTokenService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class RegisterController {

	private static final String USER_REGISTER_FORM = "user/registerForm";
	private final RegisterFormToUserMapper formToUser;
	private final PasswordEncoder encoder;
	private final UserService userService;
	private final ApplicationEventPublisher eventPublisher;
	private final VerificationTokenService verificationTokenService;

	@Autowired
	public RegisterController(RegisterFormToUserMapper formToUser, PasswordEncoder encoder, UserService userService,
			ApplicationEventPublisher eventPublisher, VerificationTokenService verificationTokenService) {
		super();
		this.formToUser = formToUser;
		this.encoder = encoder;
		this.userService = userService;
		this.eventPublisher = eventPublisher;
		this.verificationTokenService = verificationTokenService;
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
		User user = formToUser.registerFormToUser(form, encoder);
		// check if user exists in database
		if (userService.checkIfUserExists(user))
			bindingResult.rejectValue("email", "email.exists");
		// check if there are some validations errors
		if (bindingResult.hasErrors())
			return USER_REGISTER_FORM;
		else {

			userService.saveUser(user);

			// save user

			// new event to handle email sending
			eventPublisher
					.publishEvent(new OnRegistrationCompleteEvent(user, request.getLocale(), request.getContextPath()));

			return "redirect:/login?registered";
		}

	}

	@GetMapping("/confirmRegistration")
	public String confirmRegistration(Model model, @RequestParam("token") String token) {

		VerificationToken verificationToken = verificationTokenService.getToken(token);
		User user = verificationToken.getUser();
		if (verificationTokenService.checkIfExpired(verificationToken)) {
			log.debug("Token expired");
			return "redirect:/login?expired";
		}

		user.setEnabled(true);

		userService.saveUser(user);
		return "redirect:/login?enabled";
	}
}

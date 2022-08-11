package com.emesall.news.controller;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.emesall.news.dto.ChangeNameForm;
import com.emesall.news.dto.ChangePasswordForm;
import com.emesall.news.exception.NotFoundException;
import com.emesall.news.mapper.ChangeNameFormMapper;
import com.emesall.news.model.User;
import com.emesall.news.model.token.ResetPasswordToken;
import com.emesall.news.service.EmailService;
import com.emesall.news.service.UserService;
import com.emesall.news.service.token.ResetPasswordTokenService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class UserController {

	private static final String USER_SETTINGS = "user/settings";
	private static final String FORGOT_PASSWORD_TEMPLATE = "user/forgotPassword";
	private static final String CHANGE_PASSWORD_TEMPLATE = "user/changePassword";
	private final UserService userService;
	private final ChangeNameFormMapper nameFormMapper;
	private final EmailService emailService;
	private final ResetPasswordTokenService resetTokenService;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	public UserController(UserService userService, ChangeNameFormMapper nameFormMapper, EmailService emailService,
			ResetPasswordTokenService resetTokenService, PasswordEncoder passwordEncoder) {
		super();
		this.userService = userService;
		this.nameFormMapper = nameFormMapper;
		this.emailService = emailService;
		this.passwordEncoder = passwordEncoder;
		this.resetTokenService = resetTokenService;
	}

	@GetMapping("/settings")
	public String settings(Model model, @AuthenticationPrincipal User user) {

		model.addAttribute("lists", userService.findListByUser(user));

		model.addAttribute("nameForm", nameFormMapper.userToChangeNameForm(user));

		model.addAttribute("passwordForm", new ChangePasswordForm());
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

	@GetMapping("/forgotPassword")
	public String initForgotPassword() {

		return FORGOT_PASSWORD_TEMPLATE;

	}

	@PostMapping("/forgotPassword")
	public String processForgotPassword(@ModelAttribute("email") @Valid String email, BindingResult bindingResult,
			HttpServletRequest request) {

		try {
			User user = userService.findUserByEmail(email);
			if (bindingResult.hasErrors()) {
				return FORGOT_PASSWORD_TEMPLATE;
			} else {
				try {
					emailService.sendResetPasswordEmail(user, request.getContextPath(), request.getLocale());
				} catch (Exception ex) {
					return "redirect:/forgotPassword?problem";
				}
				return "redirect:/forgotPassword?sent";

			}

		} catch (NotFoundException exception) {
			return "redirect:/forgotPassword?wrongEmail";
		}

	}

	@GetMapping("/changePassword")
	public String initChangePassword(Model model, @RequestParam("token") String token) {

		ResetPasswordToken resetToken = resetTokenService.getToken(token);

		if (resetTokenService.checkIfExpired(resetToken)) {
			log.debug("Token expired");
			return "redirect:/login?expired";
		}
		ChangePasswordForm passwordForm = new ChangePasswordForm();
		passwordForm.setToken(token);
		model.addAttribute("passwordForm", passwordForm);

		return CHANGE_PASSWORD_TEMPLATE;
	}

	@PostMapping("/changePassword")
	public String processChangePassword(@ModelAttribute("passwordForm") @Valid ChangePasswordForm passwordForm,
			BindingResult bindingResult, Model model, @AuthenticationPrincipal User user) {

		// if user is logged in
		if (user != null) {
			if (bindingResult.hasErrors()) {
				return "redirect:/settings?passWrong";

			}
			user.setPassword(passwordEncoder, passwordForm.getPassword());
			userService.saveUser(user);

			return "redirect:/settings?passChanged";

			// if user forgot password and is not logged in
		} else {
			if (bindingResult.hasErrors()) {
				model.addAttribute("wrongPass", true);
				return CHANGE_PASSWORD_TEMPLATE;
			}
			ResetPasswordToken token = resetTokenService.getToken(passwordForm.getToken());
			User user_1 = token.getUser();
			user_1.setPassword(passwordEncoder, passwordForm.getPassword());
			userService.saveUser(user_1);
			return "redirect:/login?passChanged";
		}

	}

}

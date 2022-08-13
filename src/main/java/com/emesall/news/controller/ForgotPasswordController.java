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
import org.springframework.web.bind.annotation.RequestParam;

import com.emesall.news.dto.ChangePasswordForm;
import com.emesall.news.exception.NotFoundException;
import com.emesall.news.model.User;
import com.emesall.news.model.token.ResetPasswordToken;
import com.emesall.news.service.EmailService;
import com.emesall.news.service.UserService;
import com.emesall.news.service.token.ResetPasswordTokenService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ForgotPasswordController {

	private static final String FORGOT_PASSWORD_TEMPLATE = "user/forgotPassword";
	private static final String CHANGE_PASSWORD_TEMPLATE = "user/changePassword";
	private final UserService userService;
	private final EmailService emailService;
	private final ResetPasswordTokenService resetTokenService;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	public ForgotPasswordController(UserService userService, EmailService emailService,
			ResetPasswordTokenService resetTokenService, PasswordEncoder passwordEncoder) {
		super();
		this.userService = userService;
		this.emailService = emailService;
		this.passwordEncoder = passwordEncoder;
		this.resetTokenService = resetTokenService;
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
					log.debug("Sending email to reset password...");
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

	@PostMapping("/changeForgotPassword")
	public String processChangePassword(@ModelAttribute("passwordForm") @Valid ChangePasswordForm passwordForm,
			BindingResult bindingResult, Model model) {

		//find user with token assigned
		ResetPasswordToken token = resetTokenService.getToken(passwordForm.getToken());
		User user_1 = token.getUser();

		// changed password is previous one
		if (passwordEncoder.matches(passwordForm.getPassword(), user_1.getPassword())) {
			bindingResult.rejectValue("password", "TheSame.passwordForm.password");
		}

		if (bindingResult.hasErrors()) {
			return CHANGE_PASSWORD_TEMPLATE;
		}

		//change password
		user_1.setPassword(passwordEncoder, passwordForm.getPassword());
		userService.saveUser(user_1);
		// password changed, delete token
		log.debug("Password changed succesfully, reset token deleted");
		resetTokenService.deleteTokenById(token.getId());
		return "redirect:/login?passChanged";

	}

}

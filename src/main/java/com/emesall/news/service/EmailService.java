package com.emesall.news.service;

import java.util.Locale;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.emesall.news.model.User;
import com.emesall.news.model.token.ResetPasswordToken;
import com.emesall.news.service.token.ResetPasswordTokenService;

@Service
public class EmailService {

	private final SimpleEmailSender emailSender;
	private final MessageSource messages;
	private final ResetPasswordTokenService tokenService;

	@Autowired
	public EmailService(SimpleEmailSender emailSender, MessageSource messages, ResetPasswordTokenService tokenService) {
		super();
		this.emailSender = emailSender;
		this.messages = messages;
		this.tokenService = tokenService;
	}

	public void sendResetPasswordEmail(User user, String contextPath, Locale locale) {
		
		//create new token or get existing one for user
		ResetPasswordToken token = tokenService.generateToken(user);
		//prepare email
		String subject = "Reset password";
		String confirmationUrl = contextPath + "/changePassword?token=" + token.getToken();
		String messageConfirmation = messages.getMessage("changePass", null, locale);
		String welcomeMessage = messages.getMessage("welcome", null, locale);
		String message = welcomeMessage + " " + user.getFirstName() + " " + user.getLastName() + "!" + "\r\n"
				+ messageConfirmation + "\r\n" + "https://fastnews.fun" + confirmationUrl;
		emailSender.sendEmail(user.getEmail(), subject, message);

	}

}

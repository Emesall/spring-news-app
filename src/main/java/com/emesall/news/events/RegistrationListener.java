package com.emesall.news.events;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import com.emesall.news.model.User;
import com.emesall.news.service.SimpleEmailSender;
import com.emesall.news.service.token.VerificationTokenService;



@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

	private MessageSource messages;
	private SimpleEmailSender emailSender;
	private VerificationTokenService tokenService;

	@Autowired
	public RegistrationListener(MessageSource messages, SimpleEmailSender emailSender,
			VerificationTokenService tokenService) {
		super();
		this.messages = messages;
		this.tokenService = tokenService;
		this.emailSender = emailSender;
	}

	@Override
	public void onApplicationEvent(OnRegistrationCompleteEvent event) {
		confirmRegistration(event);

	}

	private void confirmRegistration(OnRegistrationCompleteEvent event) {
		User user=event.getUser();
		String token = UUID.randomUUID().toString();

		tokenService.createAndSaveToken(user, token);

		String emailAddress = user.getEmail();
		String subject = "Registration Confirmation";
		String confirmationUrl = event.getAppUrl() + "/confirmRegistration?token=" + token;
		String messageConfirmation = messages.getMessage("regSucc", null, event.getLocale());
		String welcomeMessage = messages.getMessage("welcome", null, event.getLocale());
		String message = welcomeMessage + " " + user.getFirstName() + " " + user.getLastName() + "!" + "\r\n"
				+ messageConfirmation + "\r\n" + "http://localhost:8080" + confirmationUrl;
		try {
			emailSender.sendEmail(emailAddress, subject, message);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}

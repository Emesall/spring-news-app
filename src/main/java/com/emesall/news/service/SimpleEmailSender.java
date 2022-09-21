package com.emesall.news.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.Setter;

@Service
@Setter
@ConfigurationProperties(prefix = "email")
public class SimpleEmailSender{

	private JavaMailSender mailSender;
	private String sender_email;
	
	@Autowired
	public SimpleEmailSender(JavaMailSender mailSender) {
		super();
		this.mailSender = mailSender;
	}



	public void sendEmail(String emailAddress, String subject, String message) throws MailException{
		
		SimpleMailMessage email = new SimpleMailMessage();
		email.setFrom(sender_email);
		email.setTo(emailAddress);
		email.setSubject(subject);
		email.setText(message);
		mailSender.send(email);

	}

}

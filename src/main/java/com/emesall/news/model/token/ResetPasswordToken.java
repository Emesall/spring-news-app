package com.emesall.news.model.token;

import javax.persistence.Entity;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class ResetPasswordToken extends Token {
	 
	private static final long serialVersionUID = -4066530423262438566L;

	public ResetPasswordToken(String token) {
		super(token);
		
	}
}

package com.emesall.news.model.token;

import javax.persistence.Entity;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class VerificationToken extends Token  {

	
	private static final long serialVersionUID = 1906905585385535587L;

	public VerificationToken(String token) {
		super(token);
		
	}
}

package com.emesall.news.model.token;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;

import com.emesall.news.model.BaseEntity;
import com.emesall.news.model.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "Type", discriminatorType = DiscriminatorType.STRING)
public abstract class Token extends BaseEntity {

	private static final long serialVersionUID = 4144147332782838684L;
	private static final int EXPIRATION = 60; //min

	private String token;
	@ManyToOne
	private User user;
	private Instant expirationDate;
	
	public Token(String token) {
		super();
		this.token=token;
		expirationDate=calculateExpiryDate(EXPIRATION);
	}

	private Instant calculateExpiryDate(int expiryTimeInMinutes) {
	
		Instant instant=Instant.now();
		return instant.plus(EXPIRATION, ChronoUnit.MINUTES);
		
	}
}

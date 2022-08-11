package com.emesall.news.service.token;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.emesall.news.exception.NotFoundException;
import com.emesall.news.model.User;
import com.emesall.news.model.token.Token;
import com.emesall.news.model.token.VerificationToken;
import com.emesall.news.repository.token.VerificationTokenRepository;

@Service
public class VerificationTokenService implements TokenService {

	private final VerificationTokenRepository tokenRepository;

	@Autowired
	public VerificationTokenService(VerificationTokenRepository tokenRepository) {
		super();
		this.tokenRepository = tokenRepository;
	}

	@Override
	public VerificationToken createAndSaveToken(User user, String token) {
		VerificationToken ver_token = new VerificationToken(token);
		ver_token.setUser(user);
		return save(ver_token);
	}

	@Override
	public VerificationToken getToken(String token) {
		return tokenRepository.getByToken(token)
				.orElseThrow(() -> new NotFoundException("Token is invalid. Try again"));
	}

	public VerificationToken save(VerificationToken verificationToken) {
		return tokenRepository.save(verificationToken);
	}

	public VerificationToken findByUser(User user) {
		return tokenRepository.findByUser(user);
	}

	@Override
	public boolean checkIfExpired(Token token) {
		Instant instant = Instant.now();
		if (token.getExpirationDate().isBefore(instant))
			return true;
		return false;
	}

}

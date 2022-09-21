package com.emesall.news.service.token;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.emesall.news.exception.NotFoundException;
import com.emesall.news.model.User;
import com.emesall.news.model.token.Token;
import com.emesall.news.model.token.VerificationToken;
import com.emesall.news.repository.token.VerificationTokenRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
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

	// method to check if user has token and if it's not expired
	public VerificationToken generateToken(User user) {
		// check if token was already generated for user
		VerificationToken token = findByUser(user);
		String token_str = UUID.randomUUID().toString();
		if (token == null) { // no token, generate new one
			log.debug("New verification token generated for " + user.getEmail());
			return createAndSaveToken(user, token_str);
		}

		// token expired, delete and generate new one
		if (checkIfExpired(token)) {
			deleteTokenById(token.getId());
			log.debug("Verification token expired and deleted.Generated new one for " + user.getEmail());
			return createAndSaveToken(user, token_str);
		}

		// token not expired
		log.debug("Verification token existing");
		return token;
	}

	@Override
	public void deleteTokenById(Long id) {
		tokenRepository.deleteById(id);
	}

}

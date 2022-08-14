package com.emesall.news.service.token;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.emesall.news.exception.NotFoundException;
import com.emesall.news.model.User;
import com.emesall.news.model.token.ResetPasswordToken;
import com.emesall.news.model.token.Token;
import com.emesall.news.repository.token.ResetPasswordTokenRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ResetPasswordTokenService implements TokenService {

	private final ResetPasswordTokenRepository tokenRepository;

	@Autowired
	public ResetPasswordTokenService(ResetPasswordTokenRepository tokenRepository) {
		super();
		this.tokenRepository = tokenRepository;
	}

	@Override
	public ResetPasswordToken createAndSaveToken(User user, String token) {
		ResetPasswordToken pass_token = new ResetPasswordToken(token);
		pass_token.setUser(user);
		return save(pass_token);
	}

	@Override
	public ResetPasswordToken getToken(String token) {
		return tokenRepository.getByToken(token).orElseThrow(() -> new NotFoundException("Link is invalid."));

	}

	public ResetPasswordToken save(ResetPasswordToken token) {
		return tokenRepository.save(token);
	}

	public ResetPasswordToken findByUser(User user) {
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
	public ResetPasswordToken generateToken(User user) {
		// check if token was already generated for user
		ResetPasswordToken token = findByUser(user);
		String token_str = UUID.randomUUID().toString();
		if (token == null) { // no token, generate new one
			log.debug("New reset password token generated for "+user.getEmail());
			return createAndSaveToken(user, token_str);
		}

		// token expired, delete and generate new one
		if (checkIfExpired(token)) {
			deleteTokenById(token.getId());
			log.debug("Reset password token expired and deleted.Generated new one for "+user.getEmail());
			return createAndSaveToken(user, token_str);
		}

		// token not expired
		log.debug("Reset password token existing");
		return token;
	}
	@Override
	public void deleteTokenById(Long id) {
		tokenRepository.deleteById(id);
	}

}

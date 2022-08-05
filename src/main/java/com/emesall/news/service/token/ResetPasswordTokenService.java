package com.emesall.news.service.token;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.emesall.news.exception.NotFoundException;
import com.emesall.news.model.User;
import com.emesall.news.model.token.ResetPasswordToken;
import com.emesall.news.repository.token.ResetPasswordTokenRepository;

@Service
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
		return tokenRepository.getByToken(token)
				.orElseThrow(() -> new NotFoundException("Link is invalid."));

	}

	public ResetPasswordToken save(ResetPasswordToken token) {
		return tokenRepository.save(token);
	}

}

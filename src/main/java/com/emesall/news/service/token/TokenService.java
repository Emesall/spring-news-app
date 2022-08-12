package com.emesall.news.service.token;

import com.emesall.news.model.User;
import com.emesall.news.model.token.Token;


public interface TokenService {

	Token createAndSaveToken(User user,String token);
	Token getToken(String token);
	boolean checkIfExpired(Token token);
	void deleteTokenById(Long Id);
}

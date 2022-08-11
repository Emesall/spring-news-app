package com.emesall.news.repository.token;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emesall.news.model.User;
import com.emesall.news.model.token.ResetPasswordToken;



@Repository
public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken, Long> {

	Optional<ResetPasswordToken> getByToken(String token);
	ResetPasswordToken findByUser(User user);
	
}

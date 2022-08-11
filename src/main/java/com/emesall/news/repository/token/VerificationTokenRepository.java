package com.emesall.news.repository.token;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emesall.news.model.User;
import com.emesall.news.model.token.VerificationToken;



@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

	Optional<VerificationToken> getByToken(String token);
	VerificationToken findByUser(User user);
	
}

package com.emesall.news.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emesall.news.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);
}

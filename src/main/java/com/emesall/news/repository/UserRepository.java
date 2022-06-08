package com.emesall.news.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emesall.news.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

}

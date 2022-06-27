package com.emesall.news.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emesall.news.model.Feed;

public interface FeedRepository extends JpaRepository<Feed, Long> {

	
}

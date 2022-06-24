package com.emesall.news.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emesall.news.model.Feed;

public interface FeedRepository extends JpaRepository<Feed, Long> {

	List<Feed> findAllOrderByDateTime();
}

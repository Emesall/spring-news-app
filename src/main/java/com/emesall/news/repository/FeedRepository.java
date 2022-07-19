package com.emesall.news.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.emesall.news.model.Feed;
import com.emesall.news.model.WebSite;

public interface FeedRepository extends JpaRepository<Feed, Long> {

	Page<Feed> findByWebSiteIn(List<WebSite> websites,Pageable pageable);

	
}

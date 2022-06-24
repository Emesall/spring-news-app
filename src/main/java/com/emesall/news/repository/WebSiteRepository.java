package com.emesall.news.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emesall.news.model.WebSite;

public interface WebSiteRepository extends JpaRepository<WebSite, Long> {

}

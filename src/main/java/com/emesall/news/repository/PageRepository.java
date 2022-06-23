package com.emesall.news.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emesall.news.model.Page;

public interface PageRepository extends JpaRepository<Page, Long> {

}

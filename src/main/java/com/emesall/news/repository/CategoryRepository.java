package com.emesall.news.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emesall.news.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}

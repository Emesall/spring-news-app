package com.emesall.news.service;

import com.emesall.news.model.Category;

public interface CategoryService {

	Category findByName(String name);
}

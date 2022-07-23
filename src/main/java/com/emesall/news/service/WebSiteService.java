package com.emesall.news.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.emesall.news.model.Category;
import com.emesall.news.model.WebSite;
import com.emesall.news.repository.WebSiteRepository;

@Service
public class WebSiteService {

	private final WebSiteRepository webSiteRepository;

	@Autowired
	public WebSiteService(WebSiteRepository webSiteRepository) {
		super();
		this.webSiteRepository = webSiteRepository;
	}

	public List<WebSite> findAll() {
		return webSiteRepository.findAll();
	}

	public MultiValueMap<Category, WebSite> orderWebSitesByCategory() {
		List<WebSite> websites = webSiteRepository.findAll();
		MultiValueMap<Category, WebSite> map = new LinkedMultiValueMap<>();
		for (WebSite web : websites) {
			map.add(web.getCategory(), web);
		}
		
		return map;
	}
	

}

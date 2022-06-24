package com.emesall.news.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.emesall.news.model.WebSite;
import com.emesall.news.repository.WebSiteRepository;

@Service
public class WebSiteServiceRSS implements WebSiteService {

	private final WebSiteRepository webSiteRepository;
	
	
	@Autowired
	public WebSiteServiceRSS(WebSiteRepository webSiteRepository) {
		super();
		this.webSiteRepository = webSiteRepository;
	}



	@Override
	public List<WebSite> findAll() {
		return webSiteRepository.findAll();
	}

}

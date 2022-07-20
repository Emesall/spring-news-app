package com.emesall.news.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.util.MultiValueMap;

import com.emesall.news.model.Category;
import com.emesall.news.model.WebSite;
import com.emesall.news.repository.WebSiteRepository;

class WebSiteServiceTest {

	List<WebSite> websites;
	int elements;
	WebSiteService webSiteService;
	@Mock
	WebSiteRepository webSiteRepository;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
		websites = prepareWebSites();
		webSiteService = new WebSiteService(webSiteRepository);
	}

	@Test
	void testOrderWebSitesByCategory() {
		// given
		when(webSiteRepository.findAll()).thenReturn(websites);
		// when
		MultiValueMap<Category, WebSite> map = webSiteService.orderWebSitesByCategory();
		// then
		map.values().forEach(el -> add(el.size()));

		
		assertEquals(map.size(), 3); // 3 keys-categories
		assertEquals(elements, 5); // 5 values -websites

	}

	void add(int el) {
		elements += el;

	}

	List<WebSite> prepareWebSites() throws Exception {
		// categories
		Category cat1 = new Category();
		cat1.setName("Sport");
		Category cat2 = new Category();
		cat2.setName("News");
		Category cat3 = new Category();
		cat3.setName("Politics");

		List<WebSite> sites = new ArrayList<WebSite>();
		WebSite web1 = new WebSite();
		web1.setCategory(cat1);
		web1.setUrl(new URL("https://url1.com"));
		WebSite web2 = new WebSite();
		web2.setCategory(cat1);
		web2.setUrl(new URL("https://url2.com"));
		WebSite web3 = new WebSite();
		web3.setCategory(cat2);
		web3.setUrl(new URL("https://url3.com"));
		WebSite web4 = new WebSite();
		web4.setCategory(cat3);
		web4.setUrl(new URL("https://url4.com"));
		WebSite web5 = new WebSite();
		web5.setCategory(cat2);
		web5.setUrl(new URL("https://url5.com"));
		sites.add(web1);
		sites.add(web2);
		sites.add(web3);
		sites.add(web4);
		sites.add(web5);

		return sites;

	}
}

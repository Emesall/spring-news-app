package com.emesall.news.bootstrap;

import java.net.MalformedURLException;
import java.net.URL;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.emesall.news.model.Category;
import com.emesall.news.model.WebSite;
import com.emesall.news.repository.CategoryRepository;
import com.emesall.news.repository.WebSiteRepository;

@Component
@Transactional
@Profile({ "dev", "prod" })
public class MysqlBootstrap implements ApplicationListener<ContextRefreshedEvent> {

	private final CategoryRepository categoryRepository;
	private final WebSiteRepository webSiteRepository;

	@Autowired
	public MysqlBootstrap(CategoryRepository categoryRepository, WebSiteRepository webSiteRepository) {
		super();
		this.categoryRepository = categoryRepository;
		this.webSiteRepository = webSiteRepository;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {

		try {
			if (categoryRepository.count() == 0) {
				addCategories();
			}
			if (webSiteRepository.count() == 0) {
				addWebsites();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void addWebsites() throws MalformedURLException {
		webSiteRepository.save(createWebSite("https://www.skysports.com/rss/12040", "Sport"));
		webSiteRepository.save(createWebSite("https://www.90min.com/posts.rss", "Sport"));
		webSiteRepository.save(createWebSite("https://www.dailymail.co.uk/news/articles.rss", "News"));
		webSiteRepository.save(createWebSite("https://feeds.washingtonpost.com/rss/world", "News"));
		webSiteRepository.save(createWebSite("https://feeds.a.dj.com/rss/WSJcomUSBusiness.xml", "Finance"));
		webSiteRepository.save(createWebSite("https://www.dailymail.co.uk/money/index.rss", "Finance"));
		webSiteRepository.save(createWebSite("https://www.dailymail.co.uk/health/index.rss", "Health"));
		webSiteRepository.save(createWebSite("https://www.insider.co.uk/all-about/economy/?service=rss", "Business"));
		webSiteRepository.save(createWebSite("https://www.business-live.co.uk/?service=rss", "Business"));
		webSiteRepository.save(createWebSite("https://www.techradar.com/rss", "Technology"));
		webSiteRepository.save(createWebSite("https://www.wired.com/feed/category/science/latest/rss", "Technology"));
		webSiteRepository.save(createWebSite("https://pitchfork.com/feed/feed-news/rss", "Music"));
		webSiteRepository.save(createWebSite("https://www.music-news.com/rss/UK/news?includeCover=true", "Music"));
		webSiteRepository.save(createWebSite("https://www.dailymail.co.uk/femail/food/index.rss", "Food"));
		webSiteRepository.save(createWebSite("https://www.dailymail.co.uk/travel/index.rss", "Travel"));

	}

	private WebSite createWebSite(String urlName, String catName) throws MalformedURLException {
		WebSite page = new WebSite();
		URL url = new URL(urlName);
		page.setUrl(url);
		page.setCategory(categoryRepository.findByName(catName).get());
		return page;
	}

	private Category createCategory(String name) {
		Category cat1 = new Category();
		cat1.setName(name);
		return cat1;
	}

	private void addCategories() {

		categoryRepository.save(createCategory("Sport"));
		categoryRepository.save(createCategory("News"));
		categoryRepository.save(createCategory("Finance"));
		categoryRepository.save(createCategory("Health"));
		categoryRepository.save(createCategory("Business"));
		categoryRepository.save(createCategory("Technology"));
		categoryRepository.save(createCategory("Music"));
		categoryRepository.save(createCategory("Food"));
		categoryRepository.save(createCategory("Travel"));

	}

}

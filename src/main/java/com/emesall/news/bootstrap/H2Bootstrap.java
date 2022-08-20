package com.emesall.news.bootstrap;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.Instant;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.emesall.news.model.Category;
import com.emesall.news.model.Feed;
import com.emesall.news.model.User;
import com.emesall.news.model.UserList;
import com.emesall.news.model.WebSite;
import com.emesall.news.repository.CategoryRepository;
import com.emesall.news.repository.FeedRepository;
import com.emesall.news.repository.UserListRepository;
import com.emesall.news.repository.UserRepository;
import com.emesall.news.repository.WebSiteRepository;

@Component
@Transactional
public class H2Bootstrap implements ApplicationListener<ContextRefreshedEvent> {

	private final UserRepository userRepository;
	private final CategoryRepository categoryRepository;
	private final FeedRepository feedRepository;
	private final PasswordEncoder encoder;
	private final WebSiteRepository webSiteRepository;
	private final UserListRepository userListRepository;

	@Autowired
	public H2Bootstrap(UserRepository userRepository, CategoryRepository categoryRepository,
			FeedRepository feedRepository, PasswordEncoder encoder, WebSiteRepository webSiteRepository,UserListRepository userListRepository) {
		this.userRepository = userRepository;
		this.categoryRepository = categoryRepository;
		this.feedRepository = feedRepository;
		this.encoder = encoder;
		this.webSiteRepository = webSiteRepository;
		this.userListRepository=userListRepository;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {

		
		try {
			addUser();
			addCategories();
			addWebsites();
			addList();
			//createRandomFeed(3);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	
	private void addList() {
		UserList list=new UserList();
		list.getWebSites().add(webSiteRepository.findById(1L).get());
		list.getWebSites().add(webSiteRepository.findById(2L).get());
		list.setUser(userRepository.findById(1L).get());
		list.setName("List1");
		userListRepository.save(list);
		
		UserList list2=new UserList();
		list2.getWebSites().add(webSiteRepository.findById(3L).get());
		list2.setUser(userRepository.findById(1L).get());
		list2.setName("List2");
		userListRepository.save(list2);
		
		UserList list3=new UserList();
		list3.getWebSites().add(webSiteRepository.findById(1L).get());
		list3.setUser(userRepository.findById(2L).get());
		list3.setName("List3");
		userListRepository.save(list3);
	}
	
	private void addUser() {
		User user1 = User.builder()
				.enabled(true)
				.email("user1@mail.com")
				.password(encoder.encode("user1"))
				.build();
		userRepository.save(user1);
		
		User user2 = User.builder()
				.enabled(true)
				.email("user2@mail.com")
				.password(encoder.encode("user2"))
				.build();
		
		userRepository.save(user2);
	}

	private void addWebsites() throws MalformedURLException {
		webSiteRepository.save(createWebSite("https://www.skysports.com/rss/12040", "Sport"));
		webSiteRepository.save(createWebSite("https://www.90min.com/posts.rss", "Sport"));
		webSiteRepository.save(createWebSite("http://feeds.bbci.co.uk/news/world/rss.xml", "News"));
		webSiteRepository.save(createWebSite("https://feeds.feedburner.com/euronews/en/home/", "News"));
		webSiteRepository.save(createWebSite("https://www.makingsenseofcents.com/feed", "Finance"));
		webSiteRepository.save(createWebSite("https://www.ft.com/world?format=rss", "Finance"));
		webSiteRepository.save(createWebSite("https://rssfeeds.webmd.com/rss/rss.aspx?RSSSource=RSS_PUBLIC", "Health"));
		webSiteRepository.save(createWebSite("https://www.nytimes.com/svc/collections/v1/publish/http://www.nytimes.com/section/health/rss.xml", "Health"));
		webSiteRepository.save(createWebSite("https://www.insider.co.uk/all-about/economy/?service=rss", "Business"));
		webSiteRepository.save(createWebSite("https://www.business-live.co.uk/?service=rss", "Business"));
		webSiteRepository.save(createWebSite("https://mashable.com/feeds/rss/all", "Technology"));
		webSiteRepository.save(createWebSite("https://gizmodo.com/rss", "Technology"));
		webSiteRepository.save(createWebSite("https://loudwire.com/feed/", "Music"));
		webSiteRepository.save(createWebSite("https://www.billboard.com/feed/", "Music"));
		webSiteRepository.save(createWebSite("https://therecipecritic.com/feed/", "Food"));
		webSiteRepository.save(createWebSite("https://minimalistbaker.com/feed/", "Food"));
		webSiteRepository.save(createWebSite("https://travelfreak.com/feed/", "Travel"));
		webSiteRepository.save(createWebSite("https://www.nomadicmatt.com/feed/", "Travel"));
		
		

	}
	
	private WebSite createWebSite(String urlName,String catName) throws MalformedURLException {
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

	private void createRandomFeed(int num) throws URISyntaxException {
		for (int i = 0; i < num; i++) {

			URI url = new URI("/");
			Feed feed = Feed.builder()
					.author("Author" + i)
					.instant(Instant.now())
					.entry("England shattered their own world record for the highest total in ODI cricket in the first one-dayer against Netherlands at Amstelveen on Friday (June 17). England batsman ran amok and scored 498 for 4 in 50 overs with three batsmen reaching hundreds. England beat their own record of 481 for 6 which they scored against Australia in 2018 at Nottingham.  "
							+ i)
					.title("England won over Netherland " + i)
					.uri(url)
					.build();
			List<Category> categories = categoryRepository.findAll();
			feed.getCategories().addAll(categories);
			feedRepository.save(feed);

		}
	}

}

package com.emesall.news.bootstrap;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Calendar;
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
import com.emesall.news.model.WebSite;
import com.emesall.news.repository.CategoryRepository;
import com.emesall.news.repository.FeedRepository;
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

	@Autowired
	public H2Bootstrap(UserRepository userRepository, CategoryRepository categoryRepository,
			FeedRepository feedRepository, PasswordEncoder encoder, WebSiteRepository webSiteRepository) {
		this.userRepository = userRepository;
		this.categoryRepository = categoryRepository;
		this.feedRepository = feedRepository;
		this.encoder = encoder;
		this.webSiteRepository = webSiteRepository;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		User user1 = User.builder().enabled(true).email("user1@mail.com").password(encoder.encode("user1")).build();
		userRepository.save(user1);

		addCategories();
		try {
			addWebsites();
			createRandomFeed(3);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void addWebsites() throws MalformedURLException {

		WebSite page = new WebSite();
		URL url = new URL("https://www.skysports.com/rss/12040");
		page.setUrl(url);
		page.setCategory(categoryRepository.findByName("Sport").get());
		webSiteRepository.save(page);
	}

	private void addCategories() {
		Category cat1 = new Category();
		cat1.setName("Sport");
		Category cat2 = new Category();
		cat2.setName("News");
		Category cat3 = new Category();
		cat3.setName("Olympic");
		categoryRepository.save(cat1);
		categoryRepository.save(cat2);
		categoryRepository.save(cat3);
	}

	private void createRandomFeed(int num) throws URISyntaxException {
		for (int i = 0; i < num; i++) {
			Calendar cal = Calendar.getInstance();
			cal.set(2022, 4, i + 1);

			URI url = new URI("/");
			Feed feed = Feed.builder()
					.author("Author" + i)
					.dateTime(cal.getTime())
					.entry("England shattered their own world record for the highest\r\n"
							+ "							total in ODI cricket in the first one-dayer against Netherlands\r\n"
							+ "							at Amstelveen on Friday (June 17). England batsman ran amok and\r\n"
							+ "							scored 498 for 4 in 50 overs with three batsmen reaching\r\n"
							+ "							hundreds. England beat their own record of 481 for 6 which they\r\n"
							+ "							scored against Australia in 2018 at Nottingham.  " + i)
					.title("England won over Netherland " + i)
					.uri(url)
					.build();
			List<Category> categories = categoryRepository.findAll();
			feed.getCategories().addAll(categories);

			feedRepository.save(feed);

		}
	}

}

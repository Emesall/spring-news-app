package com.emesall.news.bootstrap;

import java.net.URI;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.emesall.news.model.Category;
import com.emesall.news.model.Feed;
import com.emesall.news.model.User;
import com.emesall.news.repository.CategoryRepository;
import com.emesall.news.repository.FeedRepository;
import com.emesall.news.repository.UserRepository;

@Component
public class H2Bootstrap implements ApplicationListener<ContextRefreshedEvent> {

	private final UserRepository userRepository;
	private final CategoryRepository categoryRepository;
	private final FeedRepository feedRepository;
	private final PasswordEncoder encoder;

	@Autowired
	public H2Bootstrap(UserRepository userRepository, CategoryRepository categoryRepository,
			FeedRepository feedRepository, PasswordEncoder encoder) {
		this.userRepository = userRepository;
		this.categoryRepository = categoryRepository;
		this.feedRepository = feedRepository;
		this.encoder = encoder;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		User user1 = User.builder().enabled(true).email("user1@mail.com").password(encoder.encode("user1")).build();
		userRepository.save(user1);
		createRandomFeed(3);

	}

	private void createRandomFeed(int num) {
		for (int i = 0; i < num; i++) {
			Calendar cal = Calendar.getInstance();
			cal.set(2022, 4, i+1);
			Category cat1 = new Category();
			cat1.setName("Sport");
			Category cat2 = new Category();
			cat2.setName("News");
			Category cat3 = new Category();
			cat3.setName("Olympic");
			try {
				URI uri = new URI("/");
				Feed feed = Feed.builder()
						.author("Author"+i)
						.date_time(cal.getTime())
						.entry("England shattered their own world record for the highest\r\n"
								+ "							total in ODI cricket in the first one-dayer against Netherlands\r\n"
								+ "							at Amstelveen on Friday (June 17). England batsman ran amok and\r\n"
								+ "							scored 498 for 4 in 50 overs with three batsmen reaching\r\n"
								+ "							hundreds. England beat their own record of 481 for 6 which they\r\n"
								+ "							scored against Australia in 2018 at Nottingham.  "+i)
						.title("England won over Netherland "+i)
						.url(uri)
						.build();
				feed.getCategories().add(cat1);
				feed.getCategories().add(cat2);
				feed.getCategories().add(cat3);
				categoryRepository.save(cat1);
				categoryRepository.save(cat2);
				categoryRepository.save(cat3);
				feedRepository.save(feed);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}

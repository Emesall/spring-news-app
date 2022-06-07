package com.emesall.news.bootstrap;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class H2Bootstrap implements ApplicationListener<ContextRefreshedEvent> {

	
	
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		
		
		
	}

}

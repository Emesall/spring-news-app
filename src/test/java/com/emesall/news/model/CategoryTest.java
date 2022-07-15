package com.emesall.news.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CategoryTest {

	Category category1;
	Category category2;
	
	@BeforeEach
	void setUp() throws Exception {
		category1=new Category();

		
		category2=new Category();
		
	}

	@Test
	void testHashCode() {
		category1.setId(2L);
		category1.setName("category1");
		
		category2.setId(1L);
		category2.setName("category1");
		assertEquals(category1.hashCode(),category2.hashCode());
	}

	@Test
	void testEqualsObject() {
		category1.setId(2L);
		category1.setName("category1");
		
		category2.setId(1L);
		category2.setName("category1");
		assertTrue(category1.equals(category2));
	}
	
	@Test
	void testNotEqualsObject() {
		category1.setId(1L);
		category1.setName("category1");
		
		category2.setId(1L);
		category2.setName("category2");
		assertFalse(category1.equals(category2));
	}

}

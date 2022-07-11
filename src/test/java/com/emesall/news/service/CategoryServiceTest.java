package com.emesall.news.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.emesall.news.exception.NotFoundException;
import com.emesall.news.model.Category;
import com.emesall.news.repository.CategoryRepository;

class CategoryServiceTest {

	private static final long ID = 1L;
	private static final String NAME = "name";
	CategoryService categoryService;
	@Mock
	CategoryRepository categoryRepository;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
		categoryService = new CategoryService(categoryRepository);
	}

	@Test
	void testFindByName() {
		// given
		Category category = new Category();
		category.setName(NAME);
		category.setId(ID);
		when(categoryRepository.findByName(anyString())).thenReturn(Optional.of(category));
		// when
		Category cat = categoryService.findByName(NAME);
		assertNotNull(cat);
		assertEquals(category, cat);
		verify(categoryRepository, times(1)).findByName(anyString());

	}

	@Test
	void testFindByNameException() {
		// given
		when(categoryRepository.findByName(anyString())).thenReturn(Optional.empty());
		// when

		NotFoundException notFoundException = assertThrows(NotFoundException.class,
				() -> categoryService.findByName(NAME));
		assertTrue(notFoundException.getMessage().contains("Category "+NAME+" not found"));
		verify(categoryRepository, times(1)).findByName(anyString());
	}

}

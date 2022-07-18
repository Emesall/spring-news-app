package com.emesall.news.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.emesall.news.model.UserList;
import com.emesall.news.repository.UserListRepository;
import com.emesall.news.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

	private final UserRepository repository;
	private final UserListRepository userListRepository;

	@Autowired
	public UserService(UserRepository repository, UserListRepository userListRepository) {
		super();
		this.repository = repository;
		this.userListRepository = userListRepository;
	}

	// username==email
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		return repository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User " + email + " not found"));

	}

	public Set<UserList> findAllLists() {
		return userListRepository.findAll().stream().collect(Collectors.toSet());
	}

}

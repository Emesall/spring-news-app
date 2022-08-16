package com.emesall.news.service;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.emesall.news.exception.NotFoundException;
import com.emesall.news.model.User;
import com.emesall.news.model.UserList;
import com.emesall.news.repository.UserListRepository;

@Service
public class UserListService {

	private final UserListRepository userListRepository;

	@Autowired
	public UserListService(UserListRepository userListRepository) {
		super();
		this.userListRepository = userListRepository;
	}

	public Set<UserList> findAllLists() {
		return userListRepository.findAll().stream().collect(Collectors.toSet());
	}

	public UserList findListById(Long id) {
		return userListRepository.findById(id).orElseThrow(() -> new NotFoundException("List " + id + " not found"));

	}

	public Set<UserList> findListByUser(User user) {
		LinkedHashSet<UserList> set = new LinkedHashSet<>();
		set.addAll(userListRepository.findListByUser(user, Sort.by(Sort.Order.asc("name").ignoreCase())));
		return set;

	}

	public UserList saveUserList(UserList list) {
		return userListRepository.save(list);
	}

	public void deleteUserListById(Long id) {
		userListRepository.deleteById(id);
	}

	public boolean checkIfUserListExists(User user, UserList userList) {
		Set<UserList> allLists = findListByUser(user);
		if (allLists.contains(userList))
			return true;

		return false;
	}

	public UserList setListAsActive(UserList userList, User user) {

		// deactivate previous active list
		findListByUser(user).forEach(list -> list.setActive(false));
		// activate new one
		userList.setActive(true);

		return userList;
	}

	public Optional<UserList> returnActiveList(User user) {

		return findListByUser(user).stream().filter(list -> list.isActive()).findFirst();
	}

}

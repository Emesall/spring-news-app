package com.emesall.news.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.emesall.news.model.User;
import com.emesall.news.model.UserList;

public interface UserListRepository extends JpaRepository<UserList, Long> {

	List<UserList> findListByUser(User user,Sort sort);
}

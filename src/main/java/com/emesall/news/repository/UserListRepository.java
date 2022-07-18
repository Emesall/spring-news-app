package com.emesall.news.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emesall.news.model.UserList;

public interface UserListRepository extends JpaRepository<UserList, Long> {

}

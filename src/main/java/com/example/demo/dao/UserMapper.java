package com.example.demo.dao;

import com.example.demo.entity.Area;
import com.example.demo.entity.User;

import java.util.List;


public interface UserMapper {

	/**
	 * 插入区域信息
	 * 
	 * @param user
	 * @return
	 */
	int insertUser(User user);

	User findByUsername(String username);

	User findUserById(String id);

}

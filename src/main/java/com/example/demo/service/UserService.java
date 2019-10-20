package com.example.demo.service;

import com.example.demo.dao.UserMapper;
import com.example.demo.entity.Area;
import com.example.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


public interface UserService {
	/**
	 * 添加用户信息
	 * 
	 * @param user
	 * @return
	 */
	boolean addUser(User user);

	User findByUsername(User user);

	User findUserById(String userId);
}

package com.example.demo.service.impl;

import com.example.demo.entity.User;
import com.example.demo.dao.UserMapper;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;


	@Transactional
	@Override
	public boolean addUser(User user) {
		// 空值判断，主要是判断Name不为空
		if (user.getName() != null && !"".equals(user.getName())) {
			try {
				user.setId(UUID.randomUUID().toString());
				int effectedNum = userMapper.insertUser(user);
				if (effectedNum > 0) {
					return true;
				} else {
					throw new RuntimeException("添加用户信息失败!");
				}
			} catch (Exception e) {
				throw new RuntimeException("添加用户信息失败:" + e.toString());
			}
		} else {
			throw new RuntimeException("用户名称不能为空！");
		}
	}

	public User findByUsername(User user){
		return userMapper.findByUsername(user.getName());
	}
	public User findUserById(String userId) {
		return userMapper.findUserById(userId);
	}
}

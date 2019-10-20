package com.example.demo.service;

import com.example.demo.entity.User;

/**
 * @author wangqiang
 * @date 2019/10/15 15:22
 */
public interface TokenService {

    String getToken(User user);
}

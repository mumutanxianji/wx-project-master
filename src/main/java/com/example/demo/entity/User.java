package com.example.demo.entity;

import lombok.Data;

/**
 * @author wangqiang
 * @date 2019/10/14 11:24
 */
@Data
public class User {
    // 主键ID
    private String Id;
    // 名称
    private String name;
    // 编码
    private String password;
    // 状态
    private String status;
}

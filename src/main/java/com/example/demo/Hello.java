package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangqiang
 * @date 2019/10/14 9:31
 */
@RestController
public class Hello {

    @GetMapping("/hello")
    public String hello(){
        return "Hello Spring boot";
    }
}

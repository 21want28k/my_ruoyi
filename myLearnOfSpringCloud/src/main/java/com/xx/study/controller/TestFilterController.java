package com.xx.study.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestFilterController {


    @RequestMapping("/testFilter")
    public String testFilter(@RequestBody String s) {
        System.out.println("进入接口，接受到的body是：" + s);
        return "hello";
    }
}

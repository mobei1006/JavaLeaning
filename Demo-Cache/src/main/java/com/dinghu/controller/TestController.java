package com.dinghu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * @author 1.0
 * @author huding
 * @Date: 2024/05/22 09:57
 * @Description:
 */

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/t1")
    public String t1() {
        return "ok";
    }

    @GetMapping("/testString")
    public String test() {
        return "hello word";
    }

}

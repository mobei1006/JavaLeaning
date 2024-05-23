package com.dinghu.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}

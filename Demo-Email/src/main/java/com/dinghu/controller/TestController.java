package com.dinghu.controller;

import com.dinghu.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.util.resources.cldr.es.CalendarData_es_PY;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author 1.0
 * @author huding
 * @Date: 2024/05/22 09:57
 * @Description:
 */

@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    private EmailService emailService;

    @Value("${sendMail.to}")
    private String to;

    @Value("${sendMail.cc}")
    private String cc;

    @PostMapping("/testEmail")
    public String testEmail() {
        emailService.sendWithHtml(to, cc, "demo", "");
        return "send eamil ok";
    }

    @GetMapping("/t1")
    public String t1() {
        return "ok";
    }


}

package com.dinghu.controller;

import com.dinghu.bean.Result;
import com.dinghu.bean.User;
import com.dinghu.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.security.PublicKey;

/**
 * @author 1.0
 * @author huding
 * @Date: 2024/05/24 11:04
 * @Description:
 */

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/getUser")
    public Result getUser(@RequestParam("id") Integer id) {
        userService.getUserInfo(id);
        return Result.ok();
    }

    @PostMapping("/addUser")
    public Result addUser(@RequestBody User user) {
        userService.addUser(user);
        return Result.ok();
    }

}

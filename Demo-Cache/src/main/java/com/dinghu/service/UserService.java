package com.dinghu.service;

import com.dinghu.bean.User;

/**
 * @author 1.0
 * @author huding
 * @Date: 2024/05/23 18:43
 * @Description:
 */
public interface UserService {

    User getUserInfo(Integer id);

    void addUser(User user);

}

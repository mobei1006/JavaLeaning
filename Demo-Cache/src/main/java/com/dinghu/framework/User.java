package com.dinghu.framework;

import lombok.Data;

/**
 * @author 1.0
 * @author huding
 * @Date: 2024/05/30 16:21
 * @Description:
 */

@Data
public class User {
    private String username;

    public User(String username) {
        this.username = username;
    }
}

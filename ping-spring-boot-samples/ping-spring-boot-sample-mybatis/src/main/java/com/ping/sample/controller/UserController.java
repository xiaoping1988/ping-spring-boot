package com.ping.sample.controller;

import com.ping.sample.service.UserService;
import com.ping.sample.entity.User;
import com.ping.sample.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by liujiangping on 2017/8/20.
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("{user_id}")
    public ResponseVO<User> getByUserId(@PathVariable Long user_id) {
        return new ResponseVO<>(0, "", this.userService.findByPk(new User(user_id)));
    }
}

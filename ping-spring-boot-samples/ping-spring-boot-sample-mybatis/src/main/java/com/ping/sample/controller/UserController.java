package com.ping.sample.controller;

import com.mybatis.ping.spring.boot.enums.DbType;
import com.mybatis.ping.spring.boot.enums.Operator;
import com.mybatis.ping.spring.boot.vo.Condition;
import com.mybatis.ping.spring.boot.vo.Page;
import com.mybatis.ping.spring.boot.vo.Pagination;
import com.ping.sample.entity.User;
import com.ping.sample.service.UserService;
import com.ping.sample.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by liujiangping on 2017/8/20.
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{user_id:^\\d+$}")
    public ResponseVO<User> getByUserId(@PathVariable Long user_id) {
        return new ResponseVO<>(0, "", this.userService.findByPk(new User(user_id)));
    }

    @GetMapping("/all")
    public ResponseVO<List<User>> listAll(User user) {
        List<Condition> cons = new ArrayList<>();
        if (user != null) {
            cons.add(Condition.getCondition("user_name|string|like", user.getUser_name()));
            cons.add(new Condition("user_account", DbType.STRING, Operator.LIKE, user.getUser_account()));
        }
        return new ResponseVO<>(0, "", this.userService.find(cons));
    }

    @GetMapping("/{page:^\\d+$}/{pageSize:^\\d+$}")
    public ResponseVO<Page<User>> listPage(@PathVariable int page, @PathVariable int pageSize, User user) {
        List<Condition> cons = new ArrayList<>();
        if (user != null) {
            cons.add(Condition.getCondition("user_name|string|like", user.getUser_name()));
            cons.add(new Condition("user_account", DbType.STRING, Operator.LIKE, user.getUser_account()));
        }
        return new ResponseVO<>(0, "", this.userService.find(new Pagination(pageSize, page), cons));
    }


    @GetMapping("/count-group-by-age")
    public ResponseVO<List<Map<String, Long>>> countGroupByAge() {
        return new ResponseVO<>(0, "", this.userService.countGroupByAge());
    }

    @PostMapping("/add")
    public ResponseVO<String> addUser(@RequestBody User user) {
        this.userService.save(user);
        return new ResponseVO<>(0, "success","新增成功");
    }

    @PutMapping("/update")
    public ResponseVO<String> updateUser(@RequestBody User user) {
        this.userService.update(user);
        return new ResponseVO<>(0, "success","修改成功");
    }

    @DeleteMapping("/delete/{user_id:^\\d+$}")
    public ResponseVO<String> deleteByUserId(@PathVariable Long user_id) {
        this.userService.deleteByPk(new User(user_id));
        return new ResponseVO<>(0, "success","删除成功");
    }
}

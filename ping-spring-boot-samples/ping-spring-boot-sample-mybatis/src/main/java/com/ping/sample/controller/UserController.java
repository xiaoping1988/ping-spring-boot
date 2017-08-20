package com.ping.sample.controller;

import com.mybatis.ping.spring.boot.enums.DbType;
import com.mybatis.ping.spring.boot.enums.Operator;
import com.mybatis.ping.spring.boot.vo.Condition;
import com.mybatis.ping.spring.boot.vo.Page;
import com.mybatis.ping.spring.boot.vo.Pagination;
import com.ping.sample.entity.User;
import com.ping.sample.service.UserService;
import com.ping.sample.vo.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
@Api(description = "用户管理")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "查询一个用户")
    @GetMapping("/{user_id:^\\d+$}")
    public ResponseVO<User> getByUserId(@ApiParam(value = "用户ID", required = true) @PathVariable Long user_id) {
        return new ResponseVO<>(0, "", this.userService.findByPk(new User(user_id)));
    }

    @ApiOperation(value = "查询所有用户", notes = "查询条件:user_name like, user_account like")
    @GetMapping("/all")
    public ResponseVO<List<User>> listAll(@ApiParam(value = "用户信息") User user) {
        List<Condition> cons = new ArrayList<>();
        if (user != null) {
            cons.add(Condition.getCondition("user_name|string|like", user.getUser_name()));
            cons.add(new Condition("user_account", DbType.STRING, Operator.LIKE, user.getUser_account()));
        }
        return new ResponseVO<>(0, "", this.userService.find(cons));
    }

    @ApiOperation(value = "分页查询所有用户", notes = "查询条件:user_name like, user_account like")
    @GetMapping("/{page:^\\d+$}/{pageSize:^\\d+$}")
    public ResponseVO<Page<User>> listPage(@ApiParam(value = "当前页数", required = true) @PathVariable int page,
                                           @ApiParam(value = "页大小", required = true) @PathVariable int pageSize,
                                           @ApiParam(value = "用户信息") User user) {
        List<Condition> cons = new ArrayList<>();
        if (user != null) {
            cons.add(Condition.getCondition("user_name|string|like", user.getUser_name()));
            cons.add(new Condition("user_account", DbType.STRING, Operator.LIKE, user.getUser_account()));
        }
        return new ResponseVO<>(0, "", this.userService.find(new Pagination(pageSize, page), cons));
    }

    @ApiOperation(value = "根据用户年龄分组count")
    @GetMapping("/count-group-by-age")
    public ResponseVO<List<Map<String, Long>>> countGroupByAge() {
        return new ResponseVO<>(0, "", this.userService.countGroupByAge());
    }

    @ApiOperation(value = "新增用户")
    @PostMapping("/add")
    public ResponseVO<String> addUser(@ApiParam(value = "用户信息") @RequestBody User user) {
        this.userService.save(user);
        return new ResponseVO<>(0, "success","新增成功");
    }

    @ApiOperation(value = "修改用户")
    @PutMapping("/update")
    public ResponseVO<String> updateUser(@ApiParam(value = "用户信息") @RequestBody User user) {
        this.userService.update(user);
        return new ResponseVO<>(0, "success","修改成功");
    }

    @ApiOperation(value = "删除用户")
    @DeleteMapping("/delete/{user_id:^\\d+$}")
    public ResponseVO<String> deleteByUserId(@ApiParam(value = "用户ID", required = true) @PathVariable Long user_id) {
        this.userService.deleteByPk(new User(user_id));
        return new ResponseVO<>(0, "success","删除成功");
    }
}

package com.ping.sample.service;

import com.mybatis.ping.spring.boot.extend.service.BaseCURDService;
import com.ping.sample.entity.User;
import com.ping.sample.entity.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by liujiangping on 2017/8/20.
 */
@Service
public class UserService extends BaseCURDService<User, UserDao> {

    @Autowired
    private UserDao userDao;

    public List<Map<String, Long>> countGroupByAge() {
        return this.userDao.countGroupByAge();
    }
}

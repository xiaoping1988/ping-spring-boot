package com.ping.sample.service;

import com.mybatis.ping.spring.boot.extend.service.BaseCURDService;
import com.ping.sample.entity.User;
import com.ping.sample.entity.dao.UserDao;
import org.springframework.stereotype.Service;

/**
 * Created by liujiangping on 2017/8/20.
 */
@Service
public class UserService extends BaseCURDService<User, UserDao> {
}

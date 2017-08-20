package com.ping.sample.entity.dao;

import com.mybatis.ping.spring.boot.extend.dao.BaseCURDDao;
import com.ping.sample.entity.User;

import java.util.List;
import java.util.Map;

/**
 * 包路径,实体类路径.dao
 * 命名规则,实体名+Dao
 * Created by liujiangping on 2017/8/20.
 */
public interface UserDao extends BaseCURDDao<User> {

    public List<Map<String, Long>> countGroupByAge();
}

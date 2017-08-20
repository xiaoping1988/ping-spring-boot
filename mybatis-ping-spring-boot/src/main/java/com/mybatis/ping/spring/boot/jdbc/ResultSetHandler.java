package com.mybatis.ping.spring.boot.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by liujiangping on 2017/4/25.
 */
@FunctionalInterface
public interface ResultSetHandler<T> {

    T handle(ResultSet resultSet) throws SQLException;
}

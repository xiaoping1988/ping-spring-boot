package com.mybatis.ping.spring.boot.jdbc;

import com.mybatis.ping.spring.boot.dialect.DialectFactory;
import com.mybatis.ping.spring.boot.sqlparser.PingStatement;
import com.mybatis.ping.spring.boot.enums.DbDialect;
import com.mybatis.ping.spring.boot.vo.Pagination;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

/**
 * Created by liujiangping on 2017/4/25.
 */
public abstract class BaseJdbc {

    private static final Logger log = LoggerFactory.getLogger(BaseJdbc.class);

    protected abstract DataSource getDataSource();

    protected abstract DbDialect getDbDialect();

    private Connection getConnection() {
        try {
            return this.getDataSource().getConnection();
        } catch (SQLException e) {
            log.warn("报表数据源出错!", e);
        }
        return null;
    }

    private void close(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {

        }
    }

    /**
     * 全量查询
     *
     * @param sql             完整的sql或者是带mybatis标签的sql
     * @param parameterObject 查询条件参数
     * @param clazz
     * @param <T>
     * @return
     */
    <T> List<T> executeQuery(String sql, Map<String, Object> parameterObject, Class<T> clazz) {
        List<Map<String, Object>> res = this.executeQuery(sql, parameterObject);
        return this.populate(res, clazz);
    }

    /**
     * 分页查询
     *
     * @param sql             完整的sql或者是带mybatis标签的sql
     * @param parameterObject 查询条件参数
     * @param pagination
     * @param clazz
     * @param <T>
     * @return
     */
    <T> List<T> executeQuery(String sql, Map<String, Object> parameterObject, Pagination pagination, Class<T> clazz) {
        pagination.setTotalCount(this.executeCount(sql, parameterObject));
        List<Map<String, Object>> res = this.executeQuery(DialectFactory.getDialect(this.getDbDialect().name()).getPaginateSql(sql, pagination.getStartIndex(), pagination.getPageSize()), parameterObject);
        return this.populate(res, clazz);
    }

    private static <T> List<T> populate(List<Map<String, Object>> res, Class<T> clazz) {
        List<T> objs = new ArrayList<T>();
        try {
            T obj = null;
            for (Map<String, Object> map : res) {
                obj = clazz.newInstance();
                BeanUtils.populate(obj, map);
                objs.add(obj);
            }
            return objs;
        } catch (Exception e) {
            log.warn("", e);
        }
        return new ArrayList<T>();
    }

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "消息");
        map.put("age", 12);
        List<Map<String, Object>> list = new ArrayList<>();
        list.add(map);
        list.add(map);
        List<Test> res = populate(list, Test.class);
        System.out.println(res);
    }

    /**
     * 分页查询
     *
     * @param sql             完整的sql或者是带mybatis标签的sql
     * @param parameterObject 查询条件参数
     * @param pagination
     * @return
     */
    List<Map<String, Object>> executeQuery(String sql, Map<String, Object> parameterObject, Pagination pagination) {
        pagination.setTotalCount(this.executeCount(sql, parameterObject));
        return this.executeQuery(DialectFactory.getDialect(this.getDbDialect().name()).getPaginateSql(sql, pagination.getStartIndex(), pagination.getPageSize()), parameterObject);
    }

    /**
     * 查询sql
     *
     * @param sql             完整的sql或者是带mybatis标签的sql
     * @param parameterObject 查询条件参数
     * @return
     */
    List<Map<String, Object>> executeQuery(String sql, Map<String, Object> parameterObject) {
        List<Map<String, Object>> res = this.doQuery(sql, parameterObject, rs -> {
            ResultSetMetaData md = rs.getMetaData(); // 得到结果集(rs)的结构信息，比如字段数、字段名等
            int columnCount = md.getColumnCount(); // 返回此 ResultSet 对象中的列数
            List<String> columns = new ArrayList<>();
            for (int i = 1; i <= columnCount; i++) {
                columns.add(md.getColumnLabel(i));
            }
            List<Map<String, Object>> result = new ArrayList<>();
            Map<String, Object> rowData = null;
            while (rs.next()) {
                rowData = new LinkedHashMap<>(columnCount);
                for (String column : columns) {
                    rowData.put(column.toLowerCase(), rs.getObject(column));
                }
                result.add(rowData);
            }
            return result;
        });
        return res == null ? new ArrayList<>() : res;
    }

    /**
     * 查询sql，结果集只有一个列字段
     *
     * @param sql             完整的sql或者是带mybatis标签的sql
     * @param parameterObject 查询条件参数
     * @return
     */
    List<Object> executeQueryOneColumn(String sql, Map<String, Object> parameterObject) {
        List<Object> res = this.doQuery(sql, parameterObject, rs -> {
            List<Object> result = new ArrayList<>();
            while (rs.next()) {
                result.add(rs.getObject(1));
            }
            return result;
        });
        return res == null ? new ArrayList<>() : res;
    }

    /**
     * 查询总记录数
     *
     * @param sql             完整的sql或者是带mybatis标签的sql
     * @param parameterObject 查询条件参数
     * @return
     */
    int executeCount(String sql, Map<String, Object> parameterObject) {
        Integer res = this.doQuery("select count(*) from (" + sql + ")tt", parameterObject, rs -> {
            rs.next();
            return rs.getInt(1);
        });
        return res == null ? 0 : res;
    }

    /**
     * jdbc执行sql查询
     *
     * @param sql             完整的sql或者是带mybatis标签的sql
     * @param parameterObject 查询条件参数
     * @param handler
     * @param <T>
     * @return
     */
    private <T> T doQuery(String sql, Map<String, Object> parameterObject, ResultSetHandler<T> handler) {
//        long start = System.currentTimeMillis();
        PingStatement pingStatement = new PingStatement(sql, parameterObject, Map.class);
        log.debug(pingStatement.getSql());
        log.debug("parameters:{}", parameterObject);
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            conn = this.getConnection();
            statement = conn.prepareStatement(pingStatement.getSql());
            pingStatement.setParameters(statement);
            rs = statement.executeQuery();
            return handler.handle(rs);
        } catch (SQLException e) {
            log.warn("", e);
        } finally {
            this.close(conn, statement, rs);
            long end = System.currentTimeMillis();
//            log.info("sql耗时:{}秒!{}",(end-start)/1000,sql);
        }
        return null;
    }

    /**
     * 校验sql查询语句的合法性
     *
     * @param sql
     * @return 如果sql合法, 返回空串, 不合法则返回具体不合法信息
     */
    String validateSelect(String sql, Map<String, Object> parameterObject) {
        sql = sql + " limit 1";
        PingStatement pingStatement = new PingStatement(sql, parameterObject, null);
        log.debug(pingStatement.getSql());
        log.debug("parameters:{}", parameterObject);
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            conn = this.getConnection();
            statement = conn.prepareStatement(pingStatement.getSql());
            pingStatement.setParameters(statement);
            rs = statement.executeQuery();
            return "";
        } catch (SQLException e) {
            log.warn("", e);
            return e.getMessage();
        } finally {
            this.close(conn, statement, rs);
        }
    }
}

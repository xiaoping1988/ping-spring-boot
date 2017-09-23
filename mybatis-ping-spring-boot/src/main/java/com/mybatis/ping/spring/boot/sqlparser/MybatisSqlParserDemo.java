package com.mybatis.ping.spring.boot.sqlparser;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.scripting.xmltags.XMLScriptBuilder;
import org.apache.ibatis.session.Configuration;

import java.sql.*;
import java.util.*;

/**
 * Created by liujiangping on 2017/4/24.
 */
public class MybatisSqlParserDemo {

    public static void main(String[] args) {



        StringBuilder sqlText = new StringBuilder("");
        sqlText.append("<select>").append("select * from index_data2 where stat_date = '2017-01-13';").append("</select>");
        XPathParser parser = new XPathParser(sqlText.toString());
        XNode xNode = parser.evalNode("/select");
        Configuration configuration = new Configuration();
        XMLScriptBuilder builder = new XMLScriptBuilder(configuration,xNode,Map.class);
        SqlSource sqlSource = builder.parseScriptNode();
        Map<String,Object> params = new HashMap<>();
        params.put("start","2017-04-01");
        params.put("end","2017-04-11");
        params.put("idx_cd","USER_000001,USER_000002,USER_000003,USER_000004,USER_000005");
        BoundSql boundSql = sqlSource.getBoundSql(null);
        PingParameterHandler handler = new PingParameterHandler(configuration, null,boundSql);
        System.out.println(boundSql.getSql());
        System.out.println(boundSql.getParameterMappings());
        System.out.println(boundSql.getParameterObject());

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection("", "", "");
            stmt = conn.prepareStatement(boundSql.getSql());
            handler.setParameters(stmt);
            rs = stmt.executeQuery();
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
                System.out.println(rowData);
                result.add(rowData);
            }
            System.out.println(result);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
                stmt.close();
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

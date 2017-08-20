package com.mybatis.ping.spring.boot.sqlparser;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.scripting.xmltags.XMLScriptBuilder;
import org.apache.ibatis.session.Configuration;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by liujiangping on 2017/4/25.
 */
public class PingStatement {

    private Configuration configuration = new Configuration();
    private ParameterHandler parameterHandler;
    private Object parameterObject;
    private String originalSql;
    private XPathParser xPathParser;
    private XNode xNode;
    private XMLScriptBuilder xmlScriptBuilder;
    private BoundSql boundSql;
    private SqlSource sqlSource;

    public PingStatement(String originalSql,Object parameterObject,Class<?> parameterClass) {
        this.parameterObject = parameterObject;
        this.originalSql = originalSql;
        this.xPathParser = new XPathParser("<select>"+originalSql+"</select>");
        this.xNode = xPathParser.evalNode("/select");
        this.xmlScriptBuilder = new XMLScriptBuilder(configuration,xNode,parameterClass);
        this.sqlSource = xmlScriptBuilder.parseScriptNode();
        this.boundSql = sqlSource.getBoundSql(parameterObject);
        this.parameterHandler = new PingParameterHandler(configuration,parameterObject,boundSql);
    }

    /**
     * 获取最终jdbc执行的sql
     * @return
     */
    public String getSql(){
        return this.boundSql.getSql();
    }

    /**
     *
     * @param stmt
     * @throws SQLException
     */
    public void setParameters(PreparedStatement stmt) throws SQLException {
        this.parameterHandler.setParameters(stmt);
    }

}

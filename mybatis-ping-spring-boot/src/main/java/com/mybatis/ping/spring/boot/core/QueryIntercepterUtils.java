package com.mybatis.ping.spring.boot.core;

import java.util.List;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.MappedStatement.Builder;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.SqlSource;

public class QueryIntercepterUtils {
	public static MappedStatement copyFromMappedStatement(MappedStatement ms,
			SqlSource newSqlSource) {
		return copyFromMappedStatement(ms, newSqlSource, ms.getResultMaps());
	}

	public static MappedStatement copyFromMappedStatement(MappedStatement ms,
			SqlSource newSqlSource, List<ResultMap> maps) {
		Builder builder = new Builder(ms.getConfiguration(),
				ms.getId(), newSqlSource, ms.getSqlCommandType());
		builder.resource(ms.getResource());
		builder.fetchSize(ms.getFetchSize());
		builder.statementType(ms.getStatementType());
		builder.keyGenerator(ms.getKeyGenerator());
		// builder.keyProperty(ms.getKeyProperty());
		builder.timeout(ms.getTimeout());
		builder.parameterMap(ms.getParameterMap());
		builder.resultMaps(maps);
		builder.resultSetType(ms.getResultSetType());
		builder.cache(ms.getCache());
		builder.flushCacheRequired(ms.isFlushCacheRequired());
		builder.useCache(ms.isUseCache());

		return builder.build();
	}
}

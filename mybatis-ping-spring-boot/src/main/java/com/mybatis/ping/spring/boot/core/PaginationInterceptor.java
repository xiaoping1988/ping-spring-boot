package com.mybatis.ping.spring.boot.core;

import com.mybatis.ping.spring.boot.dialect.DialectFactory;
import com.mybatis.ping.spring.boot.meta.BaseMeta;
import com.mybatis.ping.spring.boot.vo.Pagination;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * mybatis分页插件
 * 分页查询。 通过 BoundSqlWrapper 实现SQL语句的动态替换。通过Executor调用Mybatis的查询，不直接操纵JDBC
 */

@Intercepts({ @Signature(type = Executor.class, method = "query", args = {
		MappedStatement.class, Object.class, RowBounds.class,
		ResultHandler.class }) })
@SuppressWarnings({ "rawtypes" })
public class PaginationInterceptor implements Interceptor {

	private Pattern orderByFieldPattern = Pattern.compile("[^, ]+\\s*(desc|asc){0,1}\\s*,?");
	private static final Map<String, String> countSqlMap = new HashMap<String, String>();

	public Object intercept(Invocation invocation) throws Throwable {
		MappedStatement mappedStatement = (MappedStatement) invocation
				.getArgs()[0];
		Object parameter = invocation.getArgs()[1];
		BoundSql boundSql = mappedStatement.getBoundSql(parameter);
		if (boundSql == null || StringUtils.isBlank(boundSql.getSql()))
			return null;

		Object parameterObject = boundSql.getParameterObject();
		Pagination pagination = null;
		if (parameterObject != null) {
			pagination = (Pagination) getParameterByType(parameterObject,
					Pagination.class);
		}
		if (pagination != null) {
			String originalSql = boundSql.getSql().trim();
			Integer totalCount = pagination.getTotalCount();
			// 得到总记录数
			if (totalCount <= 0) {
				// 查询记录总数。
				String countSql = getCountSql(boundSql.getSql());
				BoundSqlWrapper newBoundSql = new BoundSqlWrapper(boundSql,
						countSql, mappedStatement.getConfiguration());
				ResultMap map = new ResultMap.Builder(
						mappedStatement.getConfiguration(), "qq",
						Integer.class, new ArrayList<ResultMapping>()).build();
				List<ResultMap> mapList = new ArrayList<ResultMap>();
				mapList.add(map);
				MappedStatement newMs = QueryIntercepterUtils
						.copyFromMappedStatement(mappedStatement,
								new BoundSqlSqlSource(newBoundSql), mapList);
				List<?> rows = ((Executor) invocation.getTarget()).query(newMs,
						parameterObject, (RowBounds) invocation.getArgs()[2],
						null);
				// 在 group by sometimes return null , not 0 when the resultset
				// is empty.
				if (rows.size() == 0) {
					totalCount = 0;
				} else {
					totalCount = (Integer) rows.get(0);
				}
			}

			// 分页计算
			pagination.init(totalCount, pagination.getPageSize(),
					pagination.getCurrentPage());

			// 分页查询 本地化对象 修改数据库注意修改实现
			String pagesql = getLimitString(originalSql,
					pagination.getPageSize()
							* (pagination.getCurrentPage() - 1),
					pagination.getPageSize());

			invocation.getArgs()[2] = new RowBounds(RowBounds.NO_ROW_OFFSET,
					RowBounds.NO_ROW_LIMIT);

			BoundSqlWrapper newBoundSql = new BoundSqlWrapper(boundSql,
					pagesql, mappedStatement.getConfiguration());
			MappedStatement newMs = QueryIntercepterUtils
					.copyFromMappedStatement(mappedStatement,
							new BoundSqlSqlSource(newBoundSql));
			invocation.getArgs()[0] = newMs;
		}
		return invocation.proceed();

	}

	public static class BoundSqlWrapper extends BoundSql {
		private BoundSql sourceBoundSql;
		private String sql;

		public BoundSqlWrapper(BoundSql sourceBoundSql, String sql,
				Configuration configuration) {
			super(configuration, null, null, null);
			this.sql = sql;
			this.sourceBoundSql = sourceBoundSql;

		}

		@Override
		public String getSql() {
			return this.sql;
		}

		@Override
		public List<ParameterMapping> getParameterMappings() {
			return this.sourceBoundSql.getParameterMappings();
		}

		@Override
		public Object getParameterObject() {
			return this.sourceBoundSql.getParameterObject();
		}

		@Override
		public boolean hasAdditionalParameter(String name) {
			return this.sourceBoundSql.hasAdditionalParameter(name);
		}

		@Override
		public void setAdditionalParameter(String name, Object value) {
			this.sourceBoundSql.setAdditionalParameter(name, value);
		}

		@Override
		public Object getAdditionalParameter(String name) {
			return this.sourceBoundSql.getAdditionalParameter(name);
		}
	}

	public static class BoundSqlSqlSource implements SqlSource {
		BoundSql boundSql;

		public BoundSqlSqlSource(BoundSql boundSql) {

			this.boundSql = boundSql;
		}

		public BoundSql getBoundSql(Object parameterObject) {
			return boundSql;
		}
	}

	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	public void setProperties(Properties properties) {

	}

	/**
	 * 取参数中的类型
	 * 
	 * @param parameterObject
	 * @param parameterType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private <T> T getParameterByType(Object parameterObject,
			Class<T> parameterType) {
		if (parameterObject instanceof Map) {
			Map map = (Map) parameterObject;
			for (Object parameter : map.values()) {
				if (parameter == null)
					continue;
				if (parameterType.isAssignableFrom(parameter.getClass())) {
					return (T) parameter;
				}
			}

		} else if (parameterType.isAssignableFrom(parameterObject.getClass())) {
			return (T) parameterObject;
		}
		return null;
	}

	/**
	 * 得到分页的SQL
	 * @param querySelect 查询sql
	 * @param offset 偏移量
	 * @param limit 位置
	 * @return
	 */
	private String getLimitString(String querySelect, int offset, int limit) {
		return DialectFactory.getDialect(BaseMeta.db.toString()).getPaginateSql(querySelect, offset, limit);
	}

	/**
	 * 将SQL语句变成一条语句，并且每个单词的间隔都是1个空格
	 * 
	 * @param sql
	 *            SQL语句
	 * @return 如果sql是NULL返回空，否则返回转化后的SQL
	 */
	private String getLineSql(String sql) {
		return sql.replaceAll("[\r\n]", " ").replaceAll("\\s{2,}", " ");
	}

	/**
	 * 得到求总数的SQL 以下可能会出问题： '(' ')' ' from'在from前的常量字段中
	 * 
	 * @param sql
	 * @return
	 */
	private String getCountSql(String sql) {
		String originalSql = sql;
		// 拼一次，后面有正则什么的，还可以省点时间
		if (countSqlMap.containsKey(sql)) {
			return countSqlMap.get(sql);
		}

		sql = getLineSql(sql).trim();
		String lowercaseSQL = sql.toLowerCase();// 先规范SQL
		sql = "select count(*) from ("+lowercaseSQL+") c";
		sql = trimOrderBy(sql);
		countSqlMap.put(originalSql, sql);
		return sql;
	}

	/**
	 * 删除求和SQL中的order by 部分
	 * 
	 * @param sql
	 * @return
	 */
	private String trimOrderBy(String sql) {
		String lowercaseSQL = sql.toLowerCase();
		String orderBy = " order by";
		int orderByPosition = lowercaseSQL.lastIndexOf(orderBy);
		int rbracketPosition = lowercaseSQL.lastIndexOf(")");
		if (rbracketPosition < orderByPosition) {
			String orderbyToEndSql = lowercaseSQL.substring(orderByPosition
					+ orderBy.length());
			Matcher m = orderByFieldPattern.matcher(orderbyToEndSql);
			int endPosition = -1;
			while (m.find()) {
				if (!m.group().endsWith(",")) {
					endPosition = m.end();
					break;
				}
			}
			sql = sql.substring(0, orderByPosition)
					+ " "
					+ sql.substring(endPosition + orderByPosition
							+ orderBy.length());
		}
		return sql;
	}

	public static void main(String[] args) {
		String daoClassName = "org.ping.core.entity.dao.UserDao";
		System.out.println(daoClassName.substring(0, daoClassName.lastIndexOf(".dao.")));
	}
}
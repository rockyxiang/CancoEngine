package com.canco.pagination;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.binding.MapperMethod.MapperParamMap;
import org.apache.ibatis.builder.xml.dynamic.ForEachSqlNode;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.executor.parameter.DefaultParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.canco.pagination.dialect.Dialect;
import com.canco.pagination.dialect.OracleDialect;
import com.canco.pagination.dialect.SQL2005Dialect;

@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }) })
public class PaginationInterception implements Interceptor {

	private Logger logger = LoggerFactory
			.getLogger(PaginationInterception.class);

	public Object intercept(Invocation invocation) throws Throwable {

		StatementHandler handler = (StatementHandler) invocation.getTarget();
		MetaObject metaObject = MetaObject.forObject(handler);
		

		Page page = pageByMetaObject(metaObject);
		if (page == null || (page.getPageCount() == 0 && page.getPageSize() == 0))
			return invocation.proceed();
		
		Configuration configuration = (Configuration) metaObject.getValue("delegate.configuration");
		BoundSql orgBoundSql = (BoundSql)metaObject.getValue("delegate.boundSql");
		if( page.getCount() == 0 ){
			MappedStatement mappedStatement = (MappedStatement)metaObject.getValue("delegate.mappedStatement");
			page.setCount(totalCount((Connection)invocation.getArgs()[0], orgBoundSql, configuration, mappedStatement));
		}

		metaObject.setValue(
				"delegate.boundSql.sql",
				searchDialectByDbTypeEnum(configuration, page).spellPageSql(
						orgBoundSql.getSql(),
						page.getOffset(), page.getLimit()));
		logger.debug("pagination sql : {}", handler.getBoundSql().getSql());

		return invocation.proceed();
	}

	@SuppressWarnings("unchecked")
	private Page pageByMetaObject(MetaObject metaObject) {
		DefaultParameterHandler defaultParameterHandler = (DefaultParameterHandler) metaObject.getValue("delegate.parameterHandler");
		Object parameterObject = defaultParameterHandler.getParameterObject();
		if( parameterObject != null && parameterObject instanceof MapperParamMap ){
			MapperParamMap resultMap = (MapperParamMap)parameterObject ;
			if( !resultMap.containsKey("page")){
				return null;
			}
			
			Object pageEx = resultMap.get("page");
			if( pageEx != null)
				return (Page)pageEx;
		}
		return null ;
	}

	/**
	 * 获取方言对象
	 * 
	 * @param metaObject
	 * @param page
	 * @return
	 */
	private Dialect searchDialectByDbTypeEnum(Configuration configuration,
			Page page) {
		Dialect dialect = null;
		switch (searchDbTypeByConfig(configuration)) {
		case SQLSERVER:
			dialect = new SQL2005Dialect(page.getOrderColumn(),
					page.getOrderState());
			break;
		case MYSQL: // todo
			break;
		case DB2: // todo
			break;
		default:
			dialect = new OracleDialect(page.getOrderColumn(),
					page.getOrderState());
			break;
		}
		return dialect;
	}

	/**
	 * 获取数据库类型
	 * 
	 * @param metaObject
	 * @return 返回数据库类型的枚举对象
	 */
	private Dialect.Type searchDbTypeByConfig(Configuration configuration) {
		String dialectConfig = configuration.getVariables().getProperty("dialect");
		if (dialectConfig != null && !"".equals(dialectConfig)) {
			return Dialect.Type.valueOf(dialectConfig.toUpperCase());
		} else {
			throw new RuntimeException(
					"databaseType is null , please check your mybatis configuration!");
		}
	}
	
	/**
	 * 获取总页数
	 * @param conn
	 * @param orgBoundSql
	 * @param configuration
	 * @param mappedStatement
	 * @return
	 * @throws SQLException
	 */
	private int totalCount(Connection conn,  BoundSql orgBoundSql,
			Configuration configuration , MappedStatement mappedStatement ) throws SQLException {
		String orgSql = orgBoundSql.getSql() ;
		StringBuffer sqlBuffer = new StringBuffer(orgSql.length() + 100);
		sqlBuffer.append(" SELECT COUNT(0) FROM (").append(orgSql).append(") TEMP ");
		logger.debug(" this is countsql : {}" , sqlBuffer.toString());
		PreparedStatement preparedStatement = conn.prepareStatement(sqlBuffer.toString());
		
		Object parameterObject = orgBoundSql.getParameterObject();
		BoundSql boundSql = new BoundSql(configuration, sqlBuffer.toString(),
				orgBoundSql.getParameterMappings(),
				parameterObject);
		setParameters(preparedStatement, mappedStatement, boundSql, parameterObject);
		ResultSet rs = preparedStatement.executeQuery() ;
		if( rs != null ){
			if(rs.next()){
				return rs.getInt(1);
			}
		}
		return 0 ;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void setParameters(PreparedStatement ps,MappedStatement mappedStatement,BoundSql boundSql,Object parameterObject) throws SQLException {  
        ErrorContext.instance().activity("setting parameters").object(mappedStatement.getParameterMap().getId());  
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();  
        if (parameterMappings != null) {  
            Configuration configuration = mappedStatement.getConfiguration();  
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();  
            MetaObject metaObject = parameterObject == null ? null: configuration.newMetaObject(parameterObject);  
            for (int i = 0; i < parameterMappings.size(); i++) {  
                ParameterMapping parameterMapping = parameterMappings.get(i);  
                if (parameterMapping.getMode() != ParameterMode.OUT) {  
                    Object value;  
                    String propertyName = parameterMapping.getProperty();  
                    PropertyTokenizer prop = new PropertyTokenizer(propertyName);  
                    if (parameterObject == null) {  
                        value = null;  
                    } else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {  
                        value = parameterObject;  
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {  
                        value = boundSql.getAdditionalParameter(propertyName);  
                    } else if (propertyName.startsWith(ForEachSqlNode.ITEM_PREFIX)&& boundSql.hasAdditionalParameter(prop.getName())) {  
                        value = boundSql.getAdditionalParameter(prop.getName());  
                        if (value != null) {  
                            value = configuration.newMetaObject(value).getValue(propertyName.substring(prop.getName().length()));  
                        }  
                    } else {  
                        value = metaObject == null ? null : metaObject.getValue(propertyName);  
                    }  
                    TypeHandler typeHandler = parameterMapping.getTypeHandler();  
                    if (typeHandler == null) {  
                        throw new ExecutorException("There was no TypeHandler found for parameter "+ propertyName + " of statement "+ mappedStatement.getId());  
                    }  
                    typeHandler.setParameter(ps, i + 1, value, parameterMapping.getJdbcType());  
                }  
            }  
        }  
    }  

	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	public void setProperties(Properties properties) {
		
	}

}

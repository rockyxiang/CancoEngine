package com.canco.pagination.dialect;

public abstract class Dialect {
	
	public   static   enum  Type{  
        MYSQL,  
        ORACLE,
        SQLSERVER,
        DB2
    }  
	
      
	/**
	 * 
	 * @param sql
	 * @param offset
	 * @param limit
	 * @return
	 */
    public   abstract  String spellPageSql(String sql,  int offset,  int  limit);

	
}

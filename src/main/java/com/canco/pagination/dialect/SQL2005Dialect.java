package com.canco.pagination.dialect;

public class SQL2005Dialect extends Dialect{
	
	protected String orderColumn ;
	
	protected String orderState ;
	
	public SQL2005Dialect( String orderColumn , String orderState ){
		this.orderColumn = orderColumn;
		this.orderState = orderState;
	}

	@Override
	public String spellPageSql(String sql, int offset, int limit) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT * FROM (")
			.append(" SELECT ROW_.* , ROW_NUMBER() OVER (ORDER BY ")
			.append(orderColumn)
			.append(" ")
			.append(orderState==null?"":orderState)
			.append(") AS ROWNUM_ FROM (")
			.append(sql)
			.append(") ROW_")
			.append(") TEMP WHERE ROWNUM_>").append(offset)
			.append(" AND ROWNUM_<=").append(limit);
		return buffer.toString();
	}
	
	
}

package com.canco.pagination.dialect;

public class OracleDialect extends Dialect{
	
	private String orderColumn ;
	
	private String orderState ;
	
	public OracleDialect( String orderColumn , String orderState ){
		this.orderColumn = orderColumn ;
		this.orderState = orderState ;
	}

	@Override
	public String spellPageSql(String sql, int offset, int limit) {
		sql = sql.trim();
		StringBuffer buffer = new StringBuffer(sql.length() + 100 );
		buffer.append(" SELECT * FROM (")
			.append(" SELECT ROW_.* , ROWNUM AS ROWNUM_ FROM (")
			.append(sql)
			.append(" ORDER BY ")
			.append( orderColumn )
			.append(" ")
			.append(orderState==null?"":orderState)
			.append( ") ROW_) WHERE ROWNUM_>")
			.append(offset)
			.append(" and ROWNUM_ <= ")
			.append(limit);
		return buffer.toString();
	}
	
}

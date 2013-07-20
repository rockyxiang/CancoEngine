package com.canco.pagination;

public class Page {
	
	private int count ;
	
	/**
	 * 一页显示的数据量
	 */
	private int pageSize = 0 ;
	
	/**
	 * 当前处于第几页
	 */
	private int pageCount = 0 ;
	
	private String orderState ;
	
	private String orderColumn ;
	
	public int getOffset(){
		return (pageCount-1) * pageSize ;
	}
	
	public int getLimit(){
		return getOffset() + pageSize ;
	}

	public String getOrderState() {
		return orderState;
	}

	public void setOrderState(String orderState) {
		this.orderState = orderState;
	}

	public String getOrderColumn() {
		return orderColumn;
	}

	public void setOrderColumn(String orderColumn) {
		this.orderColumn = orderColumn;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	
}

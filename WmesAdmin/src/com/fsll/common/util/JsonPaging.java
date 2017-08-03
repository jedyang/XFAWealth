/*
 * Copyright (c) 2009-2012 by fsll
 * All rights reserved.
 */

package com.fsll.common.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 黄模建 E-mail:huangmojian@163.com
 * @version 1.0
 * Created On: Nov 10, 2010
 */
public class JsonPaging {
	
	private List list = new ArrayList();
	private Integer page = 1;//当前页数
	private Integer rows;//每页的记录数
	private Integer total = 0;//总记录数
	private String sort = "";//排序字段
	private String order = "";//asc或者dec
	
	public JsonPaging() {

	}
	
	public JsonPaging(Integer page,Integer rows,String sort,String order) {
		this.page = page;
		this.rows = rows;
		this.sort = sort;
		this.order = order;
	}

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getOrderStr() {
		String orderStr = " ";
		if(sort != null && !"".equals(sort) && order != null && !"".equals(order)){
			orderStr = " order by ";
			String[] sortArr = sort.split(",");
			String[] orderArr = order.split(",");
			for(int m=0;m<sortArr.length;m++){
				if(m==0){
					orderStr += sortArr[m]+" "+orderArr[m];
				}else{
					if (orderArr.length>m)
						orderStr += ","+sortArr[m]+" "+orderArr[m];
				}
			}
		}
		return orderStr;
	}
	
}

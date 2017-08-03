/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.core;

/**
 * 对像的返回结果
 * @author 黄模建
 * @version 1.0.0 Created On: 2016-6-12
 */
public class ResultWS{
	private String apiCode;//接口编码,所有列表vo都要返回
	private Integer curPage = 1;//当前页数
	private Integer pageSize = 10;//每页的记录数
	private Integer total = 0;//总记录数
    private String ret = "";//1成功,0失败
    private String errorCode = "";//错误编码
    private String errorMsg = "";//错误信息
    private Object data = new Object();
	public String getRet() {
		return ret;
	}
	public void setRet(String ret) {
		this.ret = ret;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public Integer getCurPage() {
		return curPage;
	}
	public void setCurPage(Integer curPage) {
		this.curPage = curPage;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public String getApiCode() {
		return apiCode;
	}
	public void setApiCode(String apiCode) {
		this.apiCode = apiCode;
	}
}

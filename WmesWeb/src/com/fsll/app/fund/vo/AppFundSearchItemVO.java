package com.fsll.app.fund.vo;

/**
 * 基金查询条件初始化通用实体
 * @author zxtan
 * @date 2017-06-01
 */
public class AppFundSearchItemVO {

	private String code;//查询条件主键或编码
	private String name;//名称
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}

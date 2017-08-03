/**
 * 
 */
package com.fsll.wmes.member.vo;

/**
 * @author scshi
 *3.4.1	判断数据项重复接口 返回结果Vo
 */
public class InfoVO {
	
	private String fieldName;//对应传入jason参数的key值
	private String duplicate;//0：不重复，1：重复
	
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getDuplicate() {
		return duplicate;
	}
	public void setDuplicate(String duplicate) {
		this.duplicate = duplicate;
	}

}

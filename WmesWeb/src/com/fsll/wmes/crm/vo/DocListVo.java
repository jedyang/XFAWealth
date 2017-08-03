/**
 * 
 */
package com.fsll.wmes.crm.vo;

import java.util.Date;

/**
 * @author scshi_u330p
 *
 */
public class DocListVo {
	private String docName;
	private Integer updateCycle;
	private String isNecessary;
	private String isValid;
	private String id;
	private Date effectDate;
	private Date createTime;
	
	private String docNameSc;
	private String docNameTc;
	private String docNameEn;
	public String getDocName() {
		return docName;
	}
	public void setDocName(String docName) {
		this.docName = docName;
	}
	public Integer getUpdateCycle() {
		return updateCycle;
	}
	public void setUpdateCycle(Integer updateCycle) {
		this.updateCycle = updateCycle;
	}
	public String getIsNecessary() {
		return isNecessary;
	}
	public void setIsNecessary(String isNecessary) {
		this.isNecessary = isNecessary;
	}
	public String getIsValid() {
		return isValid;
	}
	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getEffectDate() {
		return effectDate;
	}
	public void setEffectDate(Date effectDate) {
		this.effectDate = effectDate;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getDocNameSc() {
		return docNameSc;
	}
	public void setDocNameSc(String docNameSc) {
		this.docNameSc = docNameSc;
	}
	public String getDocNameTc() {
		return docNameTc;
	}
	public void setDocNameTc(String docNameTc) {
		this.docNameTc = docNameTc;
	}
	public String getDocNameEn() {
		return docNameEn;
	}
	public void setDocNameEn(String docNameEn) {
		this.docNameEn = docNameEn;
	}

}

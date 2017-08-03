/**
 * 
 */
package com.fsll.app.investor.me.vo;

/**
 * @author zxtan
 *
 */
public class AppRpqExamQuestItemVO {
	private String itemId;//选项Id
	private String questId;//问题Id
	private String type;//选项类型,n表示正常选项,o表示选项包含其他选项.如为其他时,可以输入单行文本
	private String title;//选项
	private String remark;//说明
	private Integer scoreValue;//选项分值
	private Integer orderBy;//排序
	
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getQuestId() {
		return questId;
	}
	public void setQuestId(String questId) {
		this.questId = questId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getScoreValue() {
		return scoreValue;
	}
	public void setScoreValue(Integer scoreValue) {
		this.scoreValue = scoreValue;
	}
	public Integer getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(Integer orderBy) {
		this.orderBy = orderBy;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}	
	
}

/**
 * 
 */
package com.fsll.app.fund.vo;

/**
 * @author scshi
 *基金公告列表数据
 */
public class AppFundAnncListDataVO {
	private String anncId;
	private String anncTitle;
	private String anncDate;
	
	public String getAnncTitle() {
		return anncTitle;
	}
	public void setAnncTitle(String anncTitle) {
		this.anncTitle = anncTitle;
	}
	public String getAnncDate() {
		return anncDate;
	}
	public void setAnncDate(String anncDate) {
		this.anncDate = anncDate;
	}
	public String getAnncId() {
		return anncId;
	}
	public void setAnncId(String anncId) {
		this.anncId = anncId;
	}

}

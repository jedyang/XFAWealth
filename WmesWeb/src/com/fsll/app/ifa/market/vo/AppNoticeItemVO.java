package com.fsll.app.ifa.market.vo;

/**
 * 公告列表实体类VO
 * @author zxtan
 * @date 2017-03-31
 */
public class AppNoticeItemVO {
	private String id;// Id
	private String subject;//标题
	private String releaseDate;//发布时间
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}	
}

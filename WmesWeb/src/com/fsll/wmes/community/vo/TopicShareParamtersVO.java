package com.fsll.wmes.community.vo;

import java.util.List;

import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.NewsInfo;

/**
 * @author wwlin
 *	当贴子信息来之分享新闻时定义的参数
 */
public class TopicShareParamtersVO {
	//贴子标题
	private String title;
	//贴子内容
	private String content;
	//分享来源类型
	private String sourceType;
	//分享来源ID
	private String sourceId;
	//查看权限 all 所有人，friend 我的好友，client 我的客户（IFA才有
	private String visitor;
	//帖子板块
	private String sectionId;
	//创建人
	private MemberBase creator;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public String getSourceType() {
		return sourceType;
	}
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	public String getSourceId() {
		return sourceId;
	}
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
	public String getVisitor() {
		return visitor;
	}
	public void setVisitor(String visitor) {
		this.visitor = visitor;
	}
	public String getSectionId() {
		return sectionId;
	}
	public void setSectionId(String sectionId) {
		this.sectionId = sectionId;
	}
	public MemberBase getCreator() {
		return creator;
	}
	public void setCreator(MemberBase creator) {
		this.creator = creator;
	}
	
	
}

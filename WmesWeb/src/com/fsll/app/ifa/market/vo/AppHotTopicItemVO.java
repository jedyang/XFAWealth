package com.fsll.app.ifa.market.vo;

/**
 * 自选策略实体类VO
 * @author zxtan
 * @date 2017-03-14
 */
public class AppHotTopicItemVO {
	private String topicId;//话题 Id
	private String topicTitle;//话题标题
	private String section;//版块
	private String isIfa;//是否IFA	
	private String creatorId;//话题发起人 Member Id
	private String creatorName;//话题发起人 姓名
	private String creatorIconUrl;//话题发起人 头像
	private String createTime;//话题创建时间
	
	
	public String getTopicId() {
		return topicId;
	}
	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}
	public String getTopicTitle() {
		return topicTitle;
	}
	public void setTopicTitle(String topicTitle) {
		this.topicTitle = topicTitle;
	}
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}	
	public String getIsIfa() {
		return isIfa;
	}
	public void setIsIfa(String isIfa) {
		this.isIfa = isIfa;
	}	
	public String getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}
	public String getCreatorName() {
		return creatorName;
	}
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}		
	public String getCreatorIconUrl() {
		return creatorIconUrl;
	}
	public void setCreatorIconUrl(String creatorIconUrl) {
		this.creatorIconUrl = creatorIconUrl;
	}	
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
}

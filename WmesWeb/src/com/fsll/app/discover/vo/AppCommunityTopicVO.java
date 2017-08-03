package com.fsll.app.discover.vo;

/**
 * 帖子观点实体类VO
 * @author zxtan
 * @date 2017-05-17
 */
public class AppCommunityTopicVO {
	private String topicId;//话题 Id
	private String topicTitle;//话题标题
	private String sectionId;//版块id
	private String sectionName;//版块名称	
	private String memberIconUrl;//发布人头像	
	private String memberId;//发布人ID	
	private String memberType;//发布人类型	
	private String memberName;//发布人昵称
	private String createTime;//创建时间
	private String sourceType;//分享来源：topic 帖子、news 新闻 、portfolio 组合、strategy 策略；
	private String sourceId;//分享来源：topic_id 帖子、news_id 新闻 、portfolio_id 组合、strategy_id 策略；
	private Object sourceObj;//分享来源实体
	private String isRecommand;//是否推荐，1是，0否
	private String recommandReason;//推荐理由
	private String commentCount;//评论数
	private String readCount;//阅读数
	private String likeCount;//点赞数
	private String unlikeCount;//踩的数
	private String transferCount;//转发次数
	private String favoriteCount;//收藏次数
	private String suportComment;//是否支持评论，1是，0否
	private String behaviorType;//赞踩行为，like unlike
	private String content;//内容
	
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
	public String getSectionId() {
		return sectionId;
	}
	public void setSectionId(String sectionId) {
		this.sectionId = sectionId;
	}
	public String getSectionName() {
		return sectionName;
	}
	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}
	public String getMemberIconUrl() {
		return memberIconUrl;
	}
	public void setMemberIconUrl(String memberIconUrl) {
		this.memberIconUrl = memberIconUrl;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}	
	public String getMemberType() {
		return memberType;
	}
	public void setMemberType(String memberType) {
		this.memberType = memberType;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
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
	public Object getSourceObj() {
		return sourceObj;
	}
	public void setSourceObj(Object sourceObj) {
		this.sourceObj = sourceObj;
	}
	public String getIsRecommand() {
		return isRecommand;
	}
	public void setIsRecommand(String isRecommand) {
		this.isRecommand = isRecommand;
	}
	public String getRecommandReason() {
		return recommandReason;
	}
	public void setRecommandReason(String recommandReason) {
		this.recommandReason = recommandReason;
	}
	public String getCommentCount() {
		return commentCount;
	}
	public void setCommentCount(String commentCount) {
		this.commentCount = commentCount;
	}
	public String getReadCount() {
		return readCount;
	}
	public void setReadCount(String readCount) {
		this.readCount = readCount;
	}
	public String getLikeCount() {
		return likeCount;
	}
	public void setLikeCount(String likeCount) {
		this.likeCount = likeCount;
	}
	public String getUnlikeCount() {
		return unlikeCount;
	}
	public void setUnlikeCount(String unlikeCount) {
		this.unlikeCount = unlikeCount;
	}
	public String getTransferCount() {
		return transferCount;
	}
	public void setTransferCount(String transferCount) {
		this.transferCount = transferCount;
	}
	public String getFavoriteCount() {
		return favoriteCount;
	}
	public void setFavoriteCount(String favoriteCount) {
		this.favoriteCount = favoriteCount;
	}
	public String getSuportComment() {
		return suportComment;
	}
	public void setSuportComment(String suportComment) {
		this.suportComment = suportComment;
	}
	public String getBehaviorType() {
		return behaviorType;
	}
	public void setBehaviorType(String behaviorType) {
		this.behaviorType = behaviorType;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}	
	
}

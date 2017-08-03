package com.fsll.app.investor.me.vo;

/**
 * 新闻列表项VO
 * @author zxtan
 * @date 2017-03-09
 */
public class AppNewsInfoItemVO {
	private String id;//新闻Id
	private String regionType;//
	private String sectionType;//
	private String flag;//文章属性，h:Top10,c:编辑推荐，f:栏目置顶，a：特荐，s：突发，b：加粗，p：有图，j:推送财金A
	private String title;//标题
	private String writer;//作者
	private String source;//来源
	private String litPic;//缩略图
	private String pubDate;//发布日期
	private String sendDate;//投稿日期
	private String keywords;//新闻关键词
	private String description;//新闻描述
	private String click;//点击次数
	private String goodPost;//点赞次数
	private String badPost;//差评次数
	private String body;//新闻内容
	private String categoryName;//栏目名称
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRegionType() {
		return regionType;
	}
	public void setRegionType(String regionType) {
		this.regionType = regionType;
	}
	public String getSectionType() {
		return sectionType;
	}
	public void setSectionType(String sectionType) {
		this.sectionType = sectionType;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getLitPic() {
		return litPic;
	}
	public void setLitPic(String litPic) {
		this.litPic = litPic;
	}
	public String getPubDate() {
		return pubDate;
	}
	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}
	public String getSendDate() {
		return sendDate;
	}
	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getClick() {
		return click;
	}
	public void setClick(String click) {
		this.click = click;
	}
	public String getGoodPost() {
		return goodPost;
	}
	public void setGoodPost(String goodPost) {
		this.goodPost = goodPost;
	}
	public String getBadPost() {
		return badPost;
	}
	public void setBadPost(String badPost) {
		this.badPost = badPost;
	}	
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}	
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
}



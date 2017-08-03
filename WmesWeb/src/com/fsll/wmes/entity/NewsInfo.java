package com.fsll.wmes.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "news_info")
public class NewsInfo implements java.io.Serializable {
	private String id;
	private String langCode;
	private String regionType;
	private String sectionType;
	private Integer xfaId;
	private Short xfaTypeId;
	private String xfaTypeId2;
	private Integer sortRank;
	private String flag;
	private Short channel;
	private Short arcrank;
	private Integer click;
	private Short money;
	private String title;
	private String shortTitle;
	private String color;
	private String writer;
	private String source;
	private String litPic;
	private Date pubDate;
	private Date sendDate;
	private Integer mid;
	private String keywords;
	private Integer totalPost;
	private String lastPost;
	private Integer scores;
	private Integer goodPost;
	private Integer badPost;
	private Boolean notPost;
	private String description;
	private Date xfaLastModifyDate;
	private String creatorId;

    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "lang_code")
	public String getLangCode() {
		return this.langCode;
	}

	public void setLangCode(String langCode) {
		this.langCode = langCode;
	}

	@Column(name = "region_type")
	public String getRegionType() {
		return this.regionType;
	}

	public void setRegionType(String regionType) {
		this.regionType = regionType;
	}

	@Column(name = "section_type")
	public String getSectionType() {
		return this.sectionType;
	}

	public void setSectionType(String sectionType) {
		this.sectionType = sectionType;
	}

	@Column(name = "xfa_id")
	public Integer getXfaId() {
		return this.xfaId;
	}

	public void setXfaId(Integer xfaId) {
		this.xfaId = xfaId;
	}

	@Column(name = "xfa_type_id")
	public Short getXfaTypeId() {
		return this.xfaTypeId;
	}

	public void setXfaTypeId(Short xfaTypeId) {
		this.xfaTypeId = xfaTypeId;
	}

	@Column(name = "xfa_type_id2")
	public String getXfaTypeId2() {
		return this.xfaTypeId2;
	}

	public void setXfaTypeId2(String xfaTypeId2) {
		this.xfaTypeId2 = xfaTypeId2;
	}

	@Column(name = "sort_rank")
	public Integer getSortRank() {
		return this.sortRank;
	}

	public void setSortRank(Integer sortRank) {
		this.sortRank = sortRank;
	}

	@Column(name = "flag")
	public String getFlag() {
		return this.flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	@Column(name = "channel")
	public Short getChannel() {
		return this.channel;
	}

	public void setChannel(Short channel) {
		this.channel = channel;
	}

	@Column(name = "arcrank")
	public Short getArcrank() {
		return this.arcrank;
	}

	public void setArcrank(Short arcrank) {
		this.arcrank = arcrank;
	}

	@Column(name = "click")
	public Integer getClick() {
		return this.click;
	}

	public void setClick(Integer click) {
		this.click = click;
	}

	@Column(name = "money")
	public Short getMoney() {
		return this.money;
	}

	public void setMoney(Short money) {
		this.money = money;
	}

	@Column(name = "title")
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "short_title")
	public String getShortTitle() {
		return this.shortTitle;
	}

	public void setShortTitle(String shortTitle) {
		this.shortTitle = shortTitle;
	}

	@Column(name = "color")
	public String getColor() {
		return this.color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	@Column(name = "writer")
	public String getWriter() {
		return this.writer;
	}

	public void setWriter(String writer) {
		this.writer = writer;
	}

	@Column(name = "source")
	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@Column(name = "lit_pic")
	public String getLitPic() {
		return this.litPic;
	}

	public void setLitPic(String litPic) {
		this.litPic = litPic;
	}

	@Column(name = "pub_date")
	public Date getPubDate() {
		return this.pubDate;
	}

	public void setPubDate(Date pubDate) {
		this.pubDate = pubDate;
	}

	@Column(name = "send_date")
	public Date getSendDate() {
		return this.sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	@Column(name = "mid")
	public Integer getMid() {
		return this.mid;
	}

	public void setMid(Integer mid) {
		this.mid = mid;
	}

	@Column(name = "keywords")
	public String getKeywords() {
		return this.keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	@Column(name = "last_post")
	public String getLastPost() {
		return this.lastPost;
	}

	public void setLastPost(String lastPost) {
		this.lastPost = lastPost;
	}

	@Column(name = "scores")
	public Integer getScores() {
		return this.scores;
	}

	public void setScores(Integer scores) {
		this.scores = scores;
	}

	@Column(name = "total_post")
	public Integer getTotalPost() {
		return totalPost;
	}

	public void setTotalPost(Integer totalPost) {
		this.totalPost = totalPost;
	}

	@Column(name = "good_post")
	public Integer getGoodPost() {
		return this.goodPost;
	}

	public void setGoodPost(Integer goodPost) {
		this.goodPost = goodPost;
	}

	@Column(name = "bad_post")
	public Integer getBadPost() {
		return this.badPost;
	}

	public void setBadPost(Integer badPost) {
		this.badPost = badPost;
	}

	@Column(name = "not_post")
	public Boolean getNotPost() {
		return this.notPost;
	}

	public void setNotPost(Boolean notPost) {
		this.notPost = notPost;
	}

	@Column(name = "description")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "xfa_last_modify_date")
	public Date getXfaLastModifyDate() {
		return this.xfaLastModifyDate;
	}

	public void setXfaLastModifyDate(Date xfaLastModifyDate) {
		this.xfaLastModifyDate = xfaLastModifyDate;
	}

	@Column(name = "creator_id")
	public String getCreatorId() {
		return this.creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

}
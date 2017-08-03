package com.fsll.wmes.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "notice")
public class Notice implements java.io.Serializable {
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "id")
	private String id;
	
	@Column(name = "subject")
	private String subject;
	
	@Column(name = "content")
	private String content;
	
	@Column(name = "target")
	private String target;

	@Column(name = "type")
	private String type;

	@Column(name = "source_type")
	private String sourceType;

	@Column(name = "create_time")
	private Date createTime;

	@Column(name = "last_update")
	private Date lastUpdate;

	@Column(name = "release_date")
	private Date releaseDate;

	@Column(name = "release_by")
	private String releaseBy;
	
	@Column(name = "level")
	private String level;
	
	@Column(name = "is_valid")
	private String isValid;
	
	@Column(name = "source")
	private String source;
	
	@Column(name = "level_type")
	private String levelType;
	
	@Column(name = "scope_type")
	private String scopeType;
	
	@Column(name = "if_ifa")
	private String ifIfa;
	
	@Column(name = "if_distributor")
	private String ifDistributor;
	
	@Column(name = "if_investor")
	private String ifInvestor;
	
	@Column(name = "product_type")
	private String productType;

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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getReleaseBy() {
		return releaseBy;
	}

	public void setReleaseBy(String releaseBy) {
		this.releaseBy = releaseBy;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getLevelType() {
		return levelType;
	}

	public void setLevelType(String levelType) {
		this.levelType = levelType;
	}

	public String getScopeType() {
		return scopeType;
	}

	public void setScopeType(String scopeType) {
		this.scopeType = scopeType;
	}

	public String getIfIfa() {
		return ifIfa;
	}

	public void setIfIfa(String ifIfa) {
		this.ifIfa = ifIfa;
	}

	public String getIfDistributor() {
		return ifDistributor;
	}

	public void setIfDistributor(String ifDistributor) {
		this.ifDistributor = ifDistributor;
	}

	public String getIfInvestor() {
		return ifInvestor;
	}

	public void setIfInvestor(String ifInvestor) {
		this.ifInvestor = ifInvestor;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

}
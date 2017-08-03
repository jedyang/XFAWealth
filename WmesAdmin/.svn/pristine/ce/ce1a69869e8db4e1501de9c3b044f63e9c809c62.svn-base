package com.fsll.wmes.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "news_info_category")
public class NewsInfoCategory implements java.io.Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
    private String id;
    
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "info_id")
	@JsonIgnore
	private NewsInfo newsInfo;
    
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "category_id")
	@JsonIgnore
	private NewsCategory category;
    
    @Column(name = "is_main")
	private String isMain;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public NewsInfo getNewsInfo() {
		return newsInfo;
	}

	public void setNewsInfo(NewsInfo newsInfo) {
		this.newsInfo = newsInfo;
	}

	public NewsCategory getCategory() {
		return category;
	}

	public void setCategory(NewsCategory category) {
		this.category = category;
	}

	public String getIsMain() {
		return isMain;
	}

	public void setIsMain(String isMain) {
		this.isMain = isMain;
	}

	
}
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
@Table(name = "news_info_images")
public class NewsInfoImages implements java.io.Serializable {
	private String id;
	private String langCode;
	private NewsInfo newsInfo;
	private Integer xfaId;
	private Short xfaTypeId;
	private String imageUrl;
	private String imageDesc;
	private String imageWidth;
	private String imageHeight;
	private Short orderNo;

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

	@Column(name = "image_url")
	public String getImageUrl() {
		return this.imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Column(name = "image_desc")
	public String getImageDesc() {
		return this.imageDesc;
	}

	public void setImageDesc(String imageDesc) {
		this.imageDesc = imageDesc;
	}

	@Column(name = "image_width")
	public String getImageWidth() {
		return this.imageWidth;
	}

	public void setImageWidth(String imageWidth) {
		this.imageWidth = imageWidth;
	}

	@Column(name = "image_height")
	public String getImageHeight() {
		return this.imageHeight;
	}

	public void setImageHeight(String imageHeight) {
		this.imageHeight = imageHeight;
	}

	@Column(name = "order_no")
	public Short getOrderNo() {
		return this.orderNo;
	}

	public void setOrderNo(Short orderNo) {
		this.orderNo = orderNo;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "info_id")
	@JsonIgnore
	public NewsInfo getNewsInfo() {
		return newsInfo;
	}

	public void setNewsInfo(NewsInfo newsInfo) {
		this.newsInfo = newsInfo;
	}

}
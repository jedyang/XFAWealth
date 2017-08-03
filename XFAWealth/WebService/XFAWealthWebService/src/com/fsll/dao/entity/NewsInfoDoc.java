package com.fsll.dao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "news_info_doc")
public class NewsInfoDoc implements java.io.Serializable {
	private String id;
	private String langCode;
	private Integer xfaId;
	private Short xfaTypeId;
	private String body;
    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "assigned")
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

	@Column(name = "body")
	public String getBody() {
		return this.body;
	}

	public void setBody(String body) {
		this.body = body;
	}

}
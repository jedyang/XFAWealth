package com.fsll.wmes.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "news_xfa_type")
public class NewsXfaType implements java.io.Serializable {
	private String id;
	private String langCode;
	private Short xfaId;
	private Short reId;
	private Short topId;
	private Short sortRank;
	private String typeName;
	private Short channelType;
	private Short isSend;
	private Short isHidden;
	private Short corank;
	private String iconUrl;
	private Boolean showOnApp;

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
	public Short getXfaId() {
		return this.xfaId;
	}

	public void setXfaId(Short xfaId) {
		this.xfaId = xfaId;
	}

	@Column(name = "re_id")
	public Short getReId() {
		return this.reId;
	}

	public void setReId(Short reId) {
		this.reId = reId;
	}

	@Column(name = "top_id")
	public Short getTopId() {
		return this.topId;
	}

	public void setTopId(Short topId) {
		this.topId = topId;
	}

	@Column(name = "sort_rank")
	public Short getSortRank() {
		return this.sortRank;
	}

	public void setSortRank(Short sortRank) {
		this.sortRank = sortRank;
	}

	@Column(name = "type_name")
	public String getTypeName() {
		return this.typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	@Column(name = "channel_type")
	public Short getChannelType() {
		return this.channelType;
	}

	public void setChannelType(Short channelType) {
		this.channelType = channelType;
	}

	@Column(name = "is_send")
	public Short getIsSend() {
		return this.isSend;
	}

	public void setIsSend(Short isSend) {
		this.isSend = isSend;
	}

	@Column(name = "is_hidden")
	public Short getIsHidden() {
		return this.isHidden;
	}

	public void setIsHidden(Short isHidden) {
		this.isHidden = isHidden;
	}

	@Column(name = "corank")
	public Short getCorank() {
		return this.corank;
	}

	public void setCorank(Short corank) {
		this.corank = corank;
	}

	@Column(name = "icon_url")
	public String getIconUrl() {
		return this.iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	@Column(name = "show_on_app")
	public Boolean getShowOnApp() {
		return this.showOnApp;
	}

	public void setShowOnApp(Boolean showOnApp) {
		this.showOnApp = showOnApp;
	}

}
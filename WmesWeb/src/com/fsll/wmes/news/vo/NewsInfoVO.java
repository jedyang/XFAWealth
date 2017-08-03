package com.fsll.wmes.news.vo;

import java.util.Date;

public class NewsInfoVO {

	private String id;
	private String title;
	private String shortTitle;
	private String description;
	private String keyWords;
	private String body;
	private String lastUpdate;
	private String upOrDown="";
	private String isFavorite;
	private String url;
	private String sectionType;
	
	private String favoritesId;
	
	private Date pubDate;
	private String pubDateFormat;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getShortTitle() {
		return shortTitle;
	}

	public void setShortTitle(String shortTitle) {
		this.shortTitle = shortTitle;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getKeyWords() {
		return keyWords;
	}

	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getUpOrDown() {
		return upOrDown;
	}

	public void setUpOrDown(String upOrDown) {
		this.upOrDown = upOrDown;
	}

	public String getIsFavorite() {
		return isFavorite;
	}

	public void setIsFavorite(String isFavorite) {
		this.isFavorite = isFavorite;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSectionType() {
		return sectionType;
	}

	public void setSectionType(String sectionType) {
		this.sectionType = sectionType;
	}

	public Date getPubDate() {
		return pubDate;
	}

	public void setPubDate(Date pubDate) {
		this.pubDate = pubDate;
	}

	public String getFavoritesId() {
		return favoritesId;
	}

	public void setFavoritesId(String favoritesId) {
		this.favoritesId = favoritesId;
	}

	public String getPubDateFormat() {
		return pubDateFormat;
	}

	public void setPubDateFormat(String pubDateFormat) {
		this.pubDateFormat = pubDateFormat;
	}
	
	
	

}

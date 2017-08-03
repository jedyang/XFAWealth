/**
 * 
 */
package com.fsll.api.product.vo;

/**
 * @author zxtan
 *	基金附件文件及下载链接信息
 */
public class FundDocDetailVO {
	private String id;
	private String linkUrl;
	private String langCode;
	private String lastUpdate;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLinkUrl() {
		return linkUrl;
	}
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
	public String getLangCode() {
		return langCode;
	}
	public void setLangCode(String langCode) {
		this.langCode = langCode;
	}
	public String getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	
	
}

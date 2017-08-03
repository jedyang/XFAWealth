package com.fsll.wmes.notice.vo;

import com.fsll.wmes.entity.MemberAdmin;

public class NoticeFilterVO {

	private String subject;
	private String releaseName;
	private String startTime;
	private String endTime;
	private String releaseEnd;
	private String target; // 发布范围，'管理员（全网 ：ALL）;IFA Firm （全网：ALL,IFA：I1,客户：I2）;Distributor:（全网：ALL,合作的IFAFIRM：D1,客户：D2）'
	private String level;
	private String sourceType; // ifafirm 投资顾问公司, distributor 代理商
	private String source; // sourceType对应的公司id
	private String keyWord;
	
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getReleaseName() {
		return releaseName;
	}
	public void setReleaseName(String releaseName) {
		this.releaseName = releaseName;
	}
	public String getReleaseEnd() {
		return releaseEnd;
	}
	public void setReleaseEnd(String releaseEnd) {
		this.releaseEnd = releaseEnd;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getSourceType() {
		return sourceType;
	}
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	
	
}

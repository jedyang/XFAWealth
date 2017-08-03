package com.fsll.wmes.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "live_setting")
public class LiveSetting implements java.io.Serializable {
	private String id;
	private String appId;
	private String channelId;
	private String channelName;
	private String channelStatus;
	private String sourceId;
	private String sourceUrl;
	private String receiveHlsUrl;
	private String receiveRtmpUrl;
	private String receiveFlvUrl;

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
	
	@Column(name = "app_id")
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	@Column(name = "channel_id")
	public String getChannelId() {
		return this.channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	@Column(name = "channel_name")
	public String getChannelName() {
		return this.channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	@Column(name = "channel_status")
	public String getChannelStatus() {
		return this.channelStatus;
	}

	public void setChannelStatus(String channelStatus) {
		this.channelStatus = channelStatus;
	}

	@Column(name = "source_id")
	public String getSourceId() {
		return this.sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	@Column(name = "source_url")
	public String getSourceUrl() {
		return this.sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}

	@Column(name = "receive_hls_url")
	public String getReceiveHlsUrl() {
		return this.receiveHlsUrl;
	}

	public void setReceiveHlsUrl(String receiveHlsUrl) {
		this.receiveHlsUrl = receiveHlsUrl;
	}

	@Column(name = "receive_rtmp_url")
	public String getReceiveRtmpUrl() {
		return this.receiveRtmpUrl;
	}

	public void setReceiveRtmpUrl(String receiveRtmpUrl) {
		this.receiveRtmpUrl = receiveRtmpUrl;
	}

	@Column(name = "receive_flv_url")
	public String getReceiveFlvUrl() {
		return this.receiveFlvUrl;
	}

	public void setReceiveFlvUrl(String receiveFlvUrl) {
		this.receiveFlvUrl = receiveFlvUrl;
	}

}
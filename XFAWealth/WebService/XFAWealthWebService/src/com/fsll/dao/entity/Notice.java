package com.fsll.dao.entity;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.sql.rowset.serial.SerialBlob;

import net.sf.ehcache.hibernate.HibernateUtil;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.engine.jdbc.SerializableBlobProxy;
import org.springframework.orm.hibernate3.support.BlobByteArrayType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mysql.jdbc.PreparedStatement;

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

	
}
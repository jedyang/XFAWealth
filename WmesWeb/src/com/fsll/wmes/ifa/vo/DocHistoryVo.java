package com.fsll.wmes.ifa.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.fsll.core.entity.AccessoryFile;

public class DocHistoryVo {
	private String id;
	private String summitDate;
	List<AccessoryFile> docAtt = new ArrayList<AccessoryFile>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSummitDate() {
		return summitDate;
	}

	public void setSummitDate(String summitDate) {
		this.summitDate = summitDate;
	}

	public List<AccessoryFile> getDocAtt() {
		return docAtt;
	}

	public void setDocAtt(List<AccessoryFile> docAtt) {
		this.docAtt = docAtt;
	}
	
}

package com.fsll.wmes.crm.vo;

import com.fsll.wmes.entity.DocList;
import com.fsll.wmes.entity.DocListEn;
import com.fsll.wmes.entity.DocListSc;
import com.fsll.wmes.entity.DocListTc;

public class DocInfoVo {
	
	private DocList doc;
	private DocListEn docEn;
	private DocListTc docTc;
	private DocListSc docSc;
	private String createTime;
	private String effectTime;
	
	public DocList getDoc() {
		return doc;
	}
	public void setDoc(DocList doc) {
		this.doc = doc;
	}
	public DocListEn getDocEn() {
		return docEn;
	}
	public void setDocEn(DocListEn docEn) {
		this.docEn = docEn;
	}
	public DocListTc getDocTc() {
		return docTc;
	}
	public void setDocTc(DocListTc docTc) {
		this.docTc = docTc;
	}
	public DocListSc getDocSc() {
		return docSc;
	}
	public void setDocSc(DocListSc docSc) {
		this.docSc = docSc;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getEffectTime() {
		return effectTime;
	}
	public void setEffectTime(String effectTime) {
		this.effectTime = effectTime;
	}
	
	
}

package com.fsll.wmes.crm.vo;

import com.fsll.wmes.entity.DocTemplate;
import com.fsll.wmes.entity.DocTemplateEn;
import com.fsll.wmes.entity.DocTemplateSc;
import com.fsll.wmes.entity.DocTemplateTc;

public class DocTemplateVo {
	private DocTemplate temp;
	private DocTemplateEn tempEn;
	private DocTemplateTc tempTc;
	private DocTemplateSc tempSc;
	public DocTemplate getTemp() {
		return temp;
	}
	public void setTemp(DocTemplate temp) {
		this.temp = temp;
	}
	public DocTemplateEn getTempEn() {
		return tempEn;
	}
	public void setTempEn(DocTemplateEn tempEn) {
		this.tempEn = tempEn;
	}
	public DocTemplateTc getTempTc() {
		return tempTc;
	}
	public void setTempTc(DocTemplateTc tempTc) {
		this.tempTc = tempTc;
	}
	public DocTemplateSc getTempSc() {
		return tempSc;
	}
	public void setTempSc(DocTemplateSc tempSc) {
		this.tempSc = tempSc;
	}
	

}

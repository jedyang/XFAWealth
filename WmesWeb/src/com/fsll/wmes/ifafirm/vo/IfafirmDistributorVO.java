package com.fsll.wmes.ifafirm.vo;

import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.MemberIfafirm;

/**
 * 运行公司-公司用户
 * @author Yan
 * @version 1.0.0 Created On: 2016-12-05
 */
public class IfafirmDistributorVO {
	
	private String id;
	private MemberIfafirm ifafirm;
	private MemberDistributor distributor;
	private String ifafirmName;

	

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public MemberIfafirm getIfafirm() {
		return ifafirm;
	}
	public void setIfafirm(MemberIfafirm ifafirm) {
		this.ifafirm = ifafirm;
	}
	public MemberDistributor getDistributor() {
		return distributor;
	}
	public void setDistributor(MemberDistributor distributor) {
		this.distributor = distributor;
	}
	public String getIfafirmName() {
		return ifafirmName;
	}
	public void setIfafirmName(String ifafirmName) {
		this.ifafirmName = ifafirmName;
	}
}

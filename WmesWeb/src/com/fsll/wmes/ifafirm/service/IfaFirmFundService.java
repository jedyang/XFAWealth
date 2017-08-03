package com.fsll.wmes.ifafirm.service;

import java.util.List;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.distributor.vo.AccountFitlerVO;
import com.fsll.wmes.entity.DistributorOrg;
import com.fsll.wmes.entity.IfafirmDistributor;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.fund.vo.FundScreenerDataVO;
import com.fsll.wmes.ifafirm.vo.MemberIfafirmBaseVO;
import com.fsll.wmes.ifafirm.vo.MemberIfafirmVO;

public interface IfaFirmFundService {
	
	/**
	 * 获取基金列表(Fund Screener页面)
	 * @param jsonPaging 分页参数
	 * @param filters 过滤条件
	 * @return
	 */
	public JsonPaging findFundInfoList(JsonPaging jsonPaging, FundScreenerDataVO filters);
	
	/**
	 * 根据ifafirmId查找所有关系的代理商
	 * @author wwlin
	 * @param memberId 人员id
	 */
	public List<IfafirmDistributor> getIfaFirmDistributorByifaFirmid(String ifafirmId);
}




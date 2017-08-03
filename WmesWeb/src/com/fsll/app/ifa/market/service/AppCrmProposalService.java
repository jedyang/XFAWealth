package com.fsll.app.ifa.market.service;

import com.fsll.app.ifa.market.vo.AppCrmProposalVO;
import com.fsll.common.util.JsonPaging;

public interface AppCrmProposalService {

	/**
	 * 得到我的投资方案列表
	 * @author zxtan
	 * @date 2017-03-29
	 * @param jsonPaging 分页
	 * @param ifaMemberId Ifa member id
	 * @param clientMemberId Client member id
	 * @param keyword 搜索关键词
	 * @param status 状态
	 * @param minAmount 初始投资额下限
	 * @param maxAmount 初始投资额上限
	 * @return
	 */
	public JsonPaging findMyProposalList(JsonPaging jsonPaging, String memberId,String keyword,String status,String minAmount,String maxAmount,String toCurrency,String langCode);
	
	/**
	 * 得到投资方案基本信息
	 * @param proposalId 方案ID
	 * @return
	 */
	public AppCrmProposalVO findProposal(String proposalId,String toCurrency,String langCode);
}

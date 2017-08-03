package com.fsll.app.ifa.client.service;

import java.util.List;

import com.fsll.app.ifa.client.vo.AppAccountBaseInfoVO;
import com.fsll.app.ifa.client.vo.AppAccountVO;
import com.fsll.app.ifa.client.vo.AppCrmProposalItemVO;
import com.fsll.common.util.JsonPaging;

/**
 * IFA客户详情接口服务类
 * @author zxtan
 * @date 2017-03-24
 *
 */
public interface AppClientDetailService {

	/**
	 * 获取客户的投资组合列表
	 * @param jsonPaging 分页信息
	 * @param ifaMemberId 
	 * @param clientMemberId
	 * @param periodCode
	 * @return
	 */
	public JsonPaging findPortfolioHoldList(JsonPaging jsonPaging,String ifaMemberId,String clientMemberId,String periodCode,String toCurrency,String langCode);
	
	/**
	 * 获取IFA客户详情的销售记录列表
	 * @author zxtan
	 * @date 2017-03-24
	 */
	public JsonPaging findCrmMemoList(JsonPaging jsonPaging,String ifaMemberId, String clientMemberId);
	
	/**
	 * 更新/删除IFA客户详情的销售记录
	 * @author zxtan
	 * @date 2017-03-24
	 */
	public boolean updateCrmMemo(String updateType, String id,	String memoText);
	
	/**
	 * IFA客户详情的账户列表
	 * @author zxtan
	 * @date 2017-03-24
	 */
	public List<AppAccountVO> findAccountList(String ifaMemberId,String clientMemberId,String toCurrency,String langCode);
	
	/**
	 * 得到投资客户的交易记录列表
	 * @author zxtan
	 * @date 2017-03-24
	 * @return
	 */
	public JsonPaging findOrderHistoryList(JsonPaging jsonPaging,String ifaMemberId, String clientMemberId,String toCurrency,String langCode);
	
	/**
	 * 获取账户基本信息
	 * @author zxtan
	 * @date 2017-03-24
	 * @return
	 */
	public AppAccountBaseInfoVO findAccountBaseInfo(String ifaMemberId,String clientMemberId,String langCode);
	
	/**
	 * 得到投资客户的投资方案列表
	 * @param ifaMemberId ifa member id
	 * @param clientMemberId 投资者member id
	 * @return
	 */
	public List<AppCrmProposalItemVO> findProposalList(String ifaMemberId,String clientMemberId,String toCurrency,String langCode);
}

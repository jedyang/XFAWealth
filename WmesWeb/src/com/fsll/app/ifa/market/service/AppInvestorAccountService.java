package com.fsll.app.ifa.market.service;

import java.util.List;

import com.fsll.app.ifa.market.vo.AppAccountBaseInfoVO;
import com.fsll.app.ifa.market.vo.AppAccountDocInfoVO;
import com.fsll.app.ifa.market.vo.AppAccountInfoVO;
import com.fsll.app.ifa.market.vo.AppAccountRpqInfoVO;
import com.fsll.app.ifa.market.vo.AppOrderHistoryVO;
import com.fsll.common.util.JsonPaging;

public interface AppInvestorAccountService {

	/**
	 * IFA客户详情的账户列表
	 * @author zxtan
	 * @date 2017-03-30
	 */
	public JsonPaging findAccountList(JsonPaging jsonPaging, String ifaMemberId,String openStatus,String langCode);
	
	/**
	 * 获取账户信息
	 * @author zxtan
	 * @date 2017-03-31
	 * @return
	 */
	public AppAccountInfoVO findAccountInfo(String accountId,String langCode);
	
	/**
	 * 得到投资账户的交易记录列表
	 * @author zxtan
	 * @date 2017-03-31
	 * @param accountId 账户ID
	 * @return
	 */
	public List<AppOrderHistoryVO> findAccountOrderHistoryList(String accountId, String toCurrency,String langCode);
	
	/**
	 * 获取账户RPQ信息
	 * @author zxtan
	 * @date 2017-03-31
	 * @return
	 */
	public AppAccountRpqInfoVO findAccountRPQInfo(String accountId,String langCode);
	
	/**
	 * 获取账户DOC信息
	 * @author zxtan
	 * @date 2017-03-31
	 * @return
	 */
	public List<AppAccountDocInfoVO> findAccountDocCheckInfo(String accountId,String langCode);
	
	/**
	 * 获取账户基本信息
	 * @author zxtan
	 * @date 2017-01-16
	 * @return
	 */
	public AppAccountBaseInfoVO findAccountBaseInfo(String accountId,String langCode);
}

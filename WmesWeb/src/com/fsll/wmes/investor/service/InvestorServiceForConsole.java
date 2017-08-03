package com.fsll.wmes.investor.service;



import java.util.List;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.InvestorAccount;
import com.fsll.wmes.investor.vo.AccountVO;

public interface InvestorServiceForConsole {

	/**
	 * 获取待审批的投资人开户列表
	 * @author mqzou
     * @date 2016-08-03
	 * @param jsonPaging
	 * @param investorAccount
	 * @return
	 */
	public JsonPaging findInvestorAccountForApproval(JsonPaging jsonPaging,InvestorAccount investorAccount);
	
	/**
	 * 获取已开户的投资账户列表
	 * @author zxtan
	 * @date 2016-08-23
	 * @param jsonPaging
	 * @param adminType
	 * @param adminMemberId
	 * @return
	 */
	public JsonPaging findInvestorAccount(JsonPaging jsonPaging,String adminType,String adminMemberId);

	/**
	 * 获取待审批列表（Ifafirm,Distributor）
	 * @author zxtan
	 * @date 2016-08-23
	 * @param jsonPaging
	 * @param adminType
	 * @param adminMemberId
	 * @param curUserId 当前用户Id modify by mqzou 2016-09-05
	 * @return
	 */
	public JsonPaging findInvestorAccountForApproval(JsonPaging jsonPaging,String adminType,String adminMemberId,String curUserId);
	

	/**
	 * 获取投资账户信息
	 * @author zxtan
	 * @date 2016-08-23
	 * @param id
	 * @return
	 */
	public InvestorAccount findInvestorAccountById(String id);
	
	/**
	 * 投资账户流程审批（Ifafirm,Distributor审批），更新状态
	 * @author zxtan
	 * @date 2016-08-24
	 * @param id
	 * @param status
	 * @return
	 */
	public Boolean updateFlowStatus(String id,String status);
	
	/**
	 * 获取投资人开户列表
	 * @author mqzou
     * @date 2016-08-23
	 * @param jsonPaging
	 * @param investorAccount
	 * @param langCode
	 * @return
	 */
	public JsonPaging findInvestorAccountList(JsonPaging jsonPaging,InvestorAccount investorAccount,String langCode);
	
	/**
	 * 获取账户信息
	 * @author mqzou
     * @date 2016-08-23
	 * @param id
	 * @return
	 */
	public InvestorAccount findById(String id);
	
	/**
	 * 获取开户的审批历史记录
	 * @author mqzou
	 * @date 2016-09-06
	 * @param accountId
	 * @return
	 */
	public List findApproveHis(String accountId);
	
	/**
	 * 获取开户的附件列表
	 * @author mqzou
	 * @date 2016-09-07
	 * @param accountId
	 * @param langCode
	 * @return
	 */
	public List findAccountFileList(String accountId,String langCode,String moduleType);

	/**
	 * 获取主账户select option
	 * @author scshi_u330p
	 * @date 20170405
	 * */
	public List findMasterAccountByDisId(String distributorId);
	
	/**
	 * 加载主账户关联的ifa，ifa公司，投资人，交易账号
	 * @author scshi_u330p
	 * @date 20170405
	 * */
	public AccountVO loadMasterAccountInfoForSub(String masterId,String langCode);
}

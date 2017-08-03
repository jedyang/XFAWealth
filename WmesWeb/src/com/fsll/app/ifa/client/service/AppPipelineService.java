package com.fsll.app.ifa.client.service;

import java.util.List;

import com.fsll.app.ifa.client.vo.AppPipelineAccountItemVO;
import com.fsll.app.ifa.client.vo.AppPipelineClientItemVO;
import com.fsll.app.ifa.client.vo.AppPipelineInvestorDetailVO;
import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.MemberDistributor;

public interface AppPipelineService {

	/**
	 * 获取开户事件的列表
	 * @param ifaMemberId
	 * @return
	 */
	public JsonPaging findAccountList(JsonPaging jsonPaging, String ifaMemberId,String keyword,String groupId,String openStatus);
	

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
	public JsonPaging findProposalList(JsonPaging jsonPaging, String ifaMemberId,String clientType,String keyword,String status,String minAmount,String maxAmount,String groupId,String langCode);
	
	/**
	 * 得到我的投资组合交易计划列表
	 * @author zxtan
	 * @date 2017-03-29
	 * @param jsonPaging 分页
	 * @param ifaMemberId Ifa member id
	 * @param keyword 搜索关键词
	 * @param status 状态
	 * @param minAmount 投资额下限
	 * @param maxAmount 投资额上限
	 * @param groupId 分组Id
	 * @param toCurrency 货币
	 * @return
	 */
	public JsonPaging findPortfolioOrderList(JsonPaging jsonPaging, String ifaMemberId,String keyword,String status,String minAmount,String maxAmount,String groupId,String toCurrency,String langCode);
	
	/**
	 * 得到我的投资组合列表
	 * @author zxtan
	 * @date 2017-03-29
	 * @param jsonPaging 分页
	 * @param ifaMemberId Ifa member id
	 * @param keyword 搜索关键词
	 * @param status 状态
	 * @param minAmount 投资额下限
	 * @param maxAmount 投资额上限
	 * @param groupId 分组Id
	 * @param toCurrency 货币
	 * @return
	 */
	public JsonPaging findPortfolioHoldList(JsonPaging jsonPaging, String ifaMemberId,String keyword,String groupId,String toCurrency,String langCode);
	
	/**
	 * 得到我的KYC列表
	 * @author zxtan
	 * @date 2017-03-29
	 * @param jsonPaging 分页
	 * @param ifaMemberId Ifa member id
	 * @param keyword 搜索关键词
	 * @param status 状态
	 * @param minAmount 投资额下限
	 * @param maxAmount 投资额上限
	 * @param groupId 分组Id
	 * @param toCurrency 货币
	 * @return
	 */
	public JsonPaging findKYCList(JsonPaging jsonPaging, String ifaMemberId,String keyword,String distributorId,String groupId);
	

	/**
	 * 得到我的未投资账户列表
	 * @author zxtan
	 * @date 2017-03-29
	 * @param jsonPaging 分页
	 * @param ifaMemberId Ifa member id
	 * @param keyword 搜索关键词
	 * @param groupId 分组Id
	 * @return
	 */
	public JsonPaging findNotYetInvestList(JsonPaging jsonPaging, String ifaMemberId,String keyword,String groupId);
	
	/**
	 * 得到我的潜在客户列表
	 * @author zxtan
	 * @date 2017-03-29
	 * @param jsonPaging 分页
	 * @param ifaMemberId Ifa member id
	 * @param keyword 搜索关键词
	 * @param groupId 分组Id
	 * @param saleStageId 潜在客户状态
	 * @return
	 */
	public JsonPaging findProspectList(JsonPaging jsonPaging, String ifaMemberId,String keyword,String groupId,String saleStageId);
	
	/**
	 * 获取代理商列表
	 * @return
	 */
	public List<MemberDistributor> findAllDistributors();
	
	/**
	 * 查找投资者
	 * @author zxtan
	 * @date 2017-04-12
	 * @param jsonPaging 分页信息
	 * @param ifaMemberId 投资顾问 member id
	 * @param languageSpoken 语言
	 * @param invStyle 投资风格
	 * @param intrest 兴趣
	 * @param noIfa 没有Ifa
	 * @param region 地区
	 * @return
	 */
	public JsonPaging findInverstorNotInCrm(JsonPaging jsonPaging, String ifaMemberId,String languageSpoken,String invStyle,String intrest,String noIfa,String region);
	
	/**
	 * 获取投资者的详情
	 * @author zxtan
	 * @date 2017-04-12
	 * @param clientMemberId
	 * @param langCode
	 * @return
	 */
	public AppPipelineInvestorDetailVO findInverstorDetail(String clientMemberId,String langCode);

	/**
	 * 添加潜在客户
	 * @author zxtan
	 * @date 2017-04-12
	 * @param ifaMemberId
	 * @param clientMemberId
	 * @param saleStageId
	 * @return
	 */
	public Boolean addCustomer(String ifaMemberId,String clientMemberId,String saleStageId);
	
	/**
	 * 得到我的客户列表
	 * @author zxtan
	 * @date 2017-04-21
	 * @param jsonPaging 分页
	 * @param ifaMemberId Ifa member id
	 * @param clientType 客户类型
	 * @param keyword 搜索关键词
	 * @param groupId 分组Id
	 * @return
	 */
	public JsonPaging findCustomerList(JsonPaging jsonPaging, String ifaMemberId,String clientType,String keyword,String groupId,String langCode);
}

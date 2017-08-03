package com.fsll.wmes.investor.service;

import java.util.List;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.OrderHistory;

public interface OrderHistoryService {
	/**
	 * 获取Ifa管理的投资账户
	 * @author zxtan
	 * @param jsonPaging
	 * @param ifaMemberId
	 * @param customerMemberId
	 * @return
	 */
	public JsonPaging findOrderHistoryListForIfa(JsonPaging jsonPaging,String ifaMemberId, String customerMemberId);

	/**
	 * 获取Ifa管理的投资账户
	 * @author zxtan
	 * @param jsonPaging
	 * @param ifaMemberId
	 * @param customerMemberId
	 * @return
	 */
	public JsonPaging findOrderHistoryListForIfa(JsonPaging jsonPaging,String langCode,String ifaMemberId, String customerMemberId,OrderHistory orderHistory);
	
	/**
	 * 根据ifa的memberId查找客户订单
	 * @author michael
	 * @param memberId
	 * @return
	 */
	@SuppressWarnings("unchecked")
    public List<OrderHistory> findOrderHistoryByIfa(String memberId);
	
	/**
	 * 检测客户订单是否存在
	 * @author michael
	 * @param ifaMemberId
	 * @param memberId
	 * @return
	 */
	public boolean checkIfExistOrderHistory(String ifaMemberId, String memberId);
	
	/**
	 * 修改IFA的客户订单到另一IFA
	 * @author michael
	 * @date 2017-3-1
	 * @param fromMemberId 源IFA
	 * @param toMemberId 目标IFA
	 * @return Boolean
	 */
	public Boolean migrateOrderHistory(String fromMemberId,String toMemberId,MemberBase createBy);
}

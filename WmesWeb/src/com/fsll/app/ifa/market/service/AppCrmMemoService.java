package com.fsll.app.ifa.market.service;

import javax.servlet.http.HttpServletRequest;

import com.fsll.app.ifa.market.vo.AppCrmMemoVO;
import com.fsll.common.util.JsonPaging;

/**
 * IFA-Market投资组合服务
 * @author zxtan
 * @date 2017-03-29
 */
public interface AppCrmMemoService {

	/**
	 * 获取IFA客户详情的销售记录列表
	 * @author zxtan
	 * @date 2017-03-24
	 */
	public JsonPaging findCrmMemoList(JsonPaging jsonPaging,String ifaMemberId, String clientMemberId);
	
	/**
	 * 获取IFA客户详情的销售记录列表
	 * @author zxtan
	 * @date 2017-03-24
	 */
	public AppCrmMemoVO findCrmMemo(String memoId);
	
	/**
	 * 更新/删除IFA客户详情的销售记录
	 * @author zxtan
	 * @date 2017-03-24
	 */
	public boolean deleteCrmMemo(String id);
	
	/**
	 * 更新/新增销售记录
	 * @author zxtan
	 * @date 2017-04-27
	 */
	public boolean saveCrmMemo(HttpServletRequest request);
}

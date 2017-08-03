package com.fsll.app.ifa.market.service;

import java.util.List;

import net.sf.json.JSONObject;

import com.fsll.app.ifa.market.vo.AppSelectMemberVO;
import com.fsll.app.ifa.market.vo.AppWebViewVO;



/**
 * 设置权限接口服务
 * @author zxtan
 * @date 2017-03-28
 */
public interface AppWebViewService {

	/**
	 * 获取我的（策略，组合...）权限
	 * @param memberId
	 * @param relateId
	 * @param langCode
	 * @return
	 */
	public AppWebViewVO findWebView(String memberId,String relateId,String langCode);	
	
	/**
	 * 获取待选择用户
	 * @author zxtan
	 * @date 2017-03-28
	 * @param memberId IfaMemberId
	 * @param userType 用户类型 prospect,client,buddy,colleague
	 * @param langCode 语言
	 * @return
	 */
	public List<AppSelectMemberVO> findToBeSelectedList(String memberId, String userType,String langCode);
	
	/**
	 * 保存我的（策略，组合...）权限
	 * @author zxtan
	 * @date 2017-03-28
	 * @param memberId
	 * @param relateId
	 * @param langCode
	 * @return
	 */
	public boolean updateWebView(JSONObject webViewObject);
}

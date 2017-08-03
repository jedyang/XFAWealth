/**
 * 
 */
package com.fsll.app.fund.service;

import com.fsll.common.util.JsonPaging;

/**
 * @author scshi
 *	基金分红派息资料接口
 */
public interface AppFundBonusService {


	/**3.1.10	获取基金分红派息信息
	 * @author scshi
	 * @param jsonPaging 分页信息
	 * @param fundId 基金id
	 * @return	jsonPaging	基金公告列表数据
	 */
	public JsonPaging fundDividendInfoList(JsonPaging jsonPaging ,String fundId);
}

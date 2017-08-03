/**
 * 
 */
package com.fsll.wmes.fund.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.fund.vo.FundDividendDataVO;

/**
 * @author scshi
 *	基金分红派息资料接口
 */
public interface FundBonusService {
	/**3.1.10	获取基金分红派息资料
	 * @author scshi
	 * @param fundId 资金id
	 * @return	<List>FundDividendDataVO	基金历史分红派息数据
	 */
	public List<FundDividendDataVO> fundDividendInfo(String fundId);

	/**3.1.10	获取基金分红派息信息
	 * @author scshi
	 * @param jsonPaging 分页信息
	 * @param fundId 基金id
	 * @return	jsonPaging	基金公告列表数据
	 */
	public JsonPaging fundDividendInfoList(JsonPaging jsonPaging ,String fundId);
}

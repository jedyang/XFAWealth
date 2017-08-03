/**
 * 
 */
package com.fsll.app.fund.service;


import java.util.List;

import com.fsll.app.fund.vo.AppFundChargesDataVO;

/**
 * @author tan
 * 基金管理费用信息接口
 * @date 20160629
 */
public interface AppFundFeesItemService {

	/**3.1.8	获取基金管理费用信息
	 * @author scshi
	 * @param fundId 资金id
	 * @param langCode 语言
	 * @return	<List>FundChargesDataVO	基金管理费用信息
	 */
	public List<AppFundChargesDataVO> fundChargesInfo(String fundId,String langCode);

}

/**
 * 
 */
package com.fsll.app.fund.service;

import java.util.List;

import com.fsll.app.fund.vo.AppFundCumulativePerformanceDataVO;
import com.fsll.app.fund.vo.AppFundYearPerformanceDataVO;

/**
 * @author scshi
 * 资金回报信息
 */
public interface AppFundReturnService {
	
	/**获取基金累积表现信息
	 * @author scshi
	 * @param fundId 基金id
	 * @param period 时期
	 * @param langCode 语言编码
	 * @return	<List>FundCumulativePerformanceDataVO	基金累积表现数据
	 */
	public List<AppFundCumulativePerformanceDataVO> fundCumulativePerformanceInfo(String fundId, String period, String langCode);
	
	/**获取基金年度表现信息
	 * @author scshi
	 * @param fundId 资金id
	 * @param year 获取的年度
	 * @param lastYears 获取上x年的数值，不包括今年（正常来说也不会有今年的数值）
	 * @return	<List>FundYearPerformanceDataVO	基金年度表现信息
	 */
	public List<AppFundYearPerformanceDataVO> fundYearPerformanceInfo(String fundId, String year, int lastYears, String langCode);
	

}

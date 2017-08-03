/**
 * 
 */
package com.fsll.wmes.fund.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.fund.vo.ChartDataVO;
import com.fsll.wmes.fund.vo.FundCumulativePerformanceDataVO;
import com.fsll.wmes.fund.vo.FundYearPerformanceDataVO;

/**
 * @author scshi
 * 资金回报信息
 */
public interface FundReturnService {
	/**3.1.5	获取基金累积表现图表数据
	 * @author scshi
	 * @param fundId 资金id
	 * @param cycle 分析周期：D-日；W-周；M-月
	 * @param period 分析时间段，按分析周期设置
	 * @param langCode 语言
	 * @return	<List>ChartDataVO	基金图表数据
	 */
	public List<ChartDataVO> fundChartData(String fundId,String cycle,String period,String langCode);
	
	/**3.1.6	获取基金累积表现信息
	 * @author scshi
	 * @param fundId 基金id
	 * @param period 时期
	 * @param langCode 语言编码
	 * @return	<List>FundCumulativePerformanceDataVO	基金累积表现数据
	 */
	public List<FundCumulativePerformanceDataVO> fundCumulativePerformanceInfo(String fundId, String period, String langCode);
	
	/**3.1.7	获取基金年度表现信息
	 * @author scshi
	 * @param fundId 资金id
	 * @param year 获取的年度
	 * @param lastYears 获取上x年的数值，不包括今年（正常来说也不会有今年的数值）
	 * @return	<List>FundYearPerformanceDataVO	基金年度表现信息
	 */
	public List<FundYearPerformanceDataVO> fundYearPerformanceInfo(String fundId, String year, int lastYears, String langCode);
	
	/**
	 * 统计全部基金所有周期的表现
	 * @author zxtan
	 * @date 2016-10-13
	 * @param jsonPaging
	 * @return 周期，最小增长值，最大增长值
	 */
	public JsonPaging findPerformanceStatList(JsonPaging jsonPaging);
}
